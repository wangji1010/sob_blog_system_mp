package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwjjbt.sob_blog_system_mp.pojo.TbImages;
import com.wwjjbt.sob_blog_system_mp.mapper.TbImagesMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbImagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@Slf4j
@Service
public class TbImagesServiceImpl implements TbImagesService {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

    @Value("${sob.blog.image.save-Path}")
    public String imagePath;

    @Value("${sob.blog.image.max-size}")
    public long maxSize;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbUserService userService;

    @Autowired
    private TbImagesMapper imagesMapper;

    /*
     * 上传图片
     * 路径可以配置
     * 命名--id，分文件夹保存
     * 保存到数据库
     * id/存储路径/url/原名称/用户id/状态/更新日期
     * */
    @Override
    public ResponseResult upLoadImage(HttpServletRequest request,HttpServletResponse response, MultipartFile file) {


        //判空
        if (file == null) {
            return ResponseResult.failed("图片不能为空");
        }

        //获取相关数据如，文件类型，文件名称
        String originalFilename = file.getOriginalFilename();

        //判断图片类型
        String contentType = file.getContentType();
        if (TextUtils.isEmpty(contentType)) {
            return ResponseResult.failed("文件格式错误");
        }
        String type = null;
        type = getType(originalFilename, contentType);
        if (type == null) {
            return ResponseResult.failed("暂不支持jpg格式图片");
        }


        log.info("============originalFilename::" + originalFilename);
        //限制文件的大小
        long size = file.getSize();
        if (size > maxSize) {
            return ResponseResult.failed("图片最大只支持" + (maxSize / 1024 / 1024) + "MB");
        }
        //创建图片的保存目录
        //规则：配置目录/日期/类型/ID.类型
        long currentMillions = System.currentTimeMillis();
        String currentDate = simpleDateFormat.format(currentMillions);
        log.info(currentDate);
        String dayPath = imagePath + File.separator + currentDate;
        File dayPathFile = new File(dayPath);
        //判断日期文件是否存在
        if (!dayPathFile.exists()) {
            dayPathFile.mkdirs();
        }
        String targetName = String.valueOf(idWorker.nextId());
        String targetPath = dayPath
                + File.separator + type + File.separator + targetName + "." + type;
        File targetFile = new File(targetPath);
        //判断类型文件是否存在
        if (!targetFile.getParentFile().exists()) {
            targetFile.mkdirs();
        }

//        //获取相关数据，类型，名称
//        File targetFile = new File(imagePath + originalFilename);
        log.info("targetFile" + targetFile);
        //根据规则命名
        //保存
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            file.transferTo(targetFile);
            //保存记录到数据库

            //返回,包含图片的名称
            Map<String, String> result = new HashMap<>();
            String resultPath = currentMillions + "_" + targetName + "." + type;
            // 返回访问路径,名称，图片描述不写前端使用名称作为描述
            result.put("path", resultPath);
            result.put("name", originalFilename);
            //记录文件
            TbImages images = new TbImages();
            images.setContentType(contentType);
            images.setId(targetName);
            images.setCreateTime(new Date());
            images.setUpdateTime(new Date());
            images.setPath(targetFile.getPath());
            images.setIName(originalFilename);
            images.setUrl(resultPath);
            images.setState("1");
            //解析出当前用户的id
            TbUser user = userService.checkUser(request, response);
            images.setUserId(user.getId());

            //保存到数据库
            imagesMapper.insert(images);

            return ResponseResult.success("上传成功").setData(result);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return ResponseResult.failed("上传失败");



    }

    private String getType(String name, String contentType) {
        String type = null;
        if (Constrants.ImageType.TYPE_PNG_WITH_PREFIX.equals(contentType)
                && name.endsWith(Constrants.ImageType.TYPE_PNG)) {
            type = Constrants.ImageType.TYPE_PNG;
        } else if (Constrants.ImageType.TYPE_GIF_WITH_PREFIX.equals(contentType)
                && name.endsWith(Constrants.ImageType.TYPE_GIF)) {
            type = Constrants.ImageType.TYPE_GIF;
        } else if (Constrants.ImageType.TYPE_JPG_WITH_PREFIX.equals(contentType)
                && name.endsWith(Constrants.ImageType.TYPE_JPG)) {
            type = Constrants.ImageType.TYPE_JPG;
        }
        return type;
    }

    private final Object mLock = new Object();
    /*
     * 拿图片
     * */
    @Override
    public void getImage(HttpServletResponse response, String imgId) {
        //根据尺寸动态返回图片给前端，减少带宽占用，传输速度快，消耗后台的cpu资源
        //上传图片的时候复制成三个尺寸  大  中  小 根据要求返回结果
        //配置的目录已知
        //需要日期
        String[] paths = imgId.split("_");
        String dayValue = paths[0];
        String format;
        synchronized (mLock){
            format = simpleDateFormat.format(Long.parseLong(dayValue));
            log.info("====>>>>"+format);
        }

        //id
        String name = paths[1];
        String type = name.substring(name.length() - 3);
        String targetPath = imagePath+File.separator+format+File.separator+type+File.separator
                +name;
        log.info("get target path ====>>>>"+targetPath);

        //需要类型

        //使用日期的时间戳
        File file = new File(targetPath);
        OutputStream writer = null;
        FileInputStream fos = null;
        try {
            response.setContentType("image/png");
            writer = response.getOutputStream();
            //读取

            fos = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = fos.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /*
    * 获取图片列表
    * */
    @Override
    public ResponseResult getImageList(int page, int size) {
        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);
        Page<TbImages> page1 = new Page<>(checkPage,checkSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByDesc("create_time");
        IPage<TbImages> tbImagesIPage = imagesMapper.selectPage(page1, wrapper);
        return ResponseResult.success("获取图片列表成功！").setData(tbImagesIPage);
    }

    /*
    * 删除图片
    * */
    @Override
    public ResponseResult deleteImageById(String imgId) {
        int i = imagesMapper.deleteById(imgId);
        if (i==0){
            return ResponseResult.failed("删除的图片不存在");
        }
        return ResponseResult.success("删除成功！");
    }
}
