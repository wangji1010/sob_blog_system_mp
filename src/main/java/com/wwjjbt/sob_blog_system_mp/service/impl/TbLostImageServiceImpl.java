package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.wwjjbt.sob_blog_system_mp.mapper.TbLostWxuserMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostImage;
import com.wwjjbt.sob_blog_system_mp.mapper.TbLostImageMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbLostWxuser;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLostImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwjjbt.sob_blog_system_mp.utils.Constrants;
import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import com.wwjjbt.sob_blog_system_mp.utils.RedisUtil;
import com.wwjjbt.sob_blog_system_mp.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangji
 * @since 2020-11-01
 */
@Slf4j
@Service
public class TbLostImageServiceImpl extends ServiceImpl<TbLostImageMapper, TbLostImage> implements TbLostImageService {

    @Value("${sob.blog.image.max-size}")
    public long maxSize;

    @Value("${sob.blog.image.save-Path}")
    public String imagePath;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbLostImageMapper lostImageMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TbLostWxuserMapper lostWxuserMapper;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

    @Override
    public String uploadImage(String key, MultipartFile file) throws JSONException {

        //解析出当前用户的openId
        //检查用户是否登录
        Object isLogin = redisUtil.get(key);
        if (isLogin==null) {
//           return ResponseResult.success("用户未登录");
           return "用户未登录";
        }
        //获取openId 查询出用户，
        JSONObject jsonObject = new JSONObject((String) isLogin);
       String openId = (String) jsonObject.get("openid");
        //判空
        if (file == null) {
//            return ResponseResult.failed("图片不能为空");
            return "图片不能为空";
        }

        //获取相关数据如，文件类型，文件名称
        String originalFilename = file.getOriginalFilename();


        //判断图片类型
        String contentType = file.getContentType();
        if (TextUtils.isEmpty(contentType)) {
//            return ResponseResult.failed("文件格式错误");
            return "文件格式错误";
        }
        String type = null;
        type = getType(originalFilename, contentType);
        if (type == null) {
//            return ResponseResult.failed("暂不支持jpg格式图片");
            return "请检查网络";
        }


        log.info("============originalFilename::" + originalFilename);
        //限制文件的大小
        long size = file.getSize();
        if (size > maxSize) {
//            return ResponseResult.failed("图片最大只支持" + (maxSize / 1024 / 1024) + "MB");
            return "图片最大只支持" + (maxSize / 1024 / 1024) + "MB";
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
            TbLostImage lostImage = new TbLostImage();
            lostImage.setContentType(contentType);
            lostImage.setId(targetName);
            lostImage.setCreateTime(new Date());
            lostImage.setUpdateTime(new Date());
            lostImage.setPath(targetFile.getPath());
            lostImage.setName(originalFilename);
            lostImage.setUrl(resultPath);
            lostImage.setState("1");
            //解析出当前用户的id
            TbLostWxuser tbLostWxuser = lostWxuserMapper.selectByOS(openId);
            String wxuserId = tbLostWxuser.getId();
            lostImage.setOpenId(wxuserId);

            //保存到数据库
            lostImageMapper.insert(lostImage);

//            return ResponseResult.success("上传成功").setData(result);
            return resultPath;

        } catch (IOException e) {
            e.printStackTrace();

        }
        return "上传失败";
    }

    private String getType(String name, String contentType) {
        String type = null;
        if (Constrants.ImageType.TYPE_PNG_WITH_PREFIX.equals(contentType)
                && name.endsWith(Constrants.ImageType.TYPE_PNG)) {
            type = Constrants.ImageType.TYPE_PNG;
        } else if (Constrants.ImageType.TYPE_GIF_WITH_PREFIX.equals(contentType)
                && name.endsWith(Constrants.ImageType.TYPE_GIF)) {
            type = Constrants.ImageType.TYPE_GIF;
        } else if ((Constrants.ImageType.TYPE_JPG_WITH_PREFIX.equals(contentType)
                ||Constrants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(contentType))
                && name.endsWith(Constrants.ImageType.TYPE_JPG)) {
            type = Constrants.ImageType.TYPE_JPG;
        }
        return type;
    }
    private final Object mLock = new Object();
    /*
    * 获取单张图片
    * */
    @Override
    public void getImageById(HttpServletResponse response, String imageId) {
        //根据尺寸动态返回图片给前端，减少带宽占用，传输速度快，消耗后台的cpu资源
        //上传图片的时候复制成三个尺寸  大  中  小 根据要求返回结果
        //配置的目录已知
        //需要日期
        String[] paths = imageId.split("_");
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


}
