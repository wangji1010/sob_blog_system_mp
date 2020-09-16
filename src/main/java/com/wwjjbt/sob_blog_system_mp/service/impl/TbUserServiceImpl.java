package com.wwjjbt.sob_blog_system_mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wwjjbt.sob_blog_system_mp.mapper.TbRefreshTokenMapper;
import com.wwjjbt.sob_blog_system_mp.mapper.TbSettingsMapper;
import com.wwjjbt.sob_blog_system_mp.pojo.TbRefreshToken;
import com.wwjjbt.sob_blog_system_mp.pojo.TbSettings;
import com.wwjjbt.sob_blog_system_mp.pojo.TbUser;
import com.wwjjbt.sob_blog_system_mp.mapper.TbUserMapper;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbUserService;
import com.wwjjbt.sob_blog_system_mp.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
@Transactional//事务
public class TbUserServiceImpl implements TbUserService {

    //图灵验证码，字体
    public static final int[] CAPTCHA_FONT_TYPES = {Captcha.FONT_1
            , Captcha.FONT_2
            , Captcha.FONT_3
            , Captcha.FONT_4
            , Captcha.FONT_5
            , Captcha.FONT_6
            , Captcha.FONT_7
            , Captcha.FONT_8
            , Captcha.FONT_9
            , Captcha.FONT_10
    };
    public static final int[] CAPTCHA_TYPE = {Captcha.TYPE_DEFAULT
            , Captcha.TYPE_ONLY_NUMBER
            , Captcha.TYPE_ONLY_CHAR
            , Captcha.TYPE_ONLY_UPPER
            , Captcha.TYPE_ONLY_LOWER
            , Captcha.TYPE_NUM_AND_UPPER
    };

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private TbSettingsMapper settingsMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Random random;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TbRefreshTokenMapper refreshTokenMapper;

    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    @Override
    public ResponseResult initManagerCount(TbUser user, HttpServletRequest request) {
        //检查是否有初始化
        TbSettings settings = settingsMapper.hasKey(Constrants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        if (settings != null) {
            return ResponseResult.failed("管理员账号已经初始化了");
        }

        //检查数据
        if (TextUtils.isEmpty(user.getUserName())) {
            return ResponseResult.failed("用户名不能为空");
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            return ResponseResult.failed("密码不能为空");
        }
        if (TextUtils.isEmpty(user.getEmail())) {
            return ResponseResult.failed("email不能为空");
        }
        //补充数据
        user.setId(String.valueOf(idWorker.nextId()));
        user.setRoles(Constrants.User.ROLE_ADMIN);
        user.setAvatar(Constrants.User.DEFAULT_AVATAR);
        user.setState(Constrants.User.DEFAULT_STATE);
        String remoteAddr = request.getRemoteAddr();
        String localAddr = request.getLocalAddr();
        log.info("==============================remoteAddr==>:" + remoteAddr);
        log.info("==============================localAddr==>:" + localAddr);
        user.setRegIp(remoteAddr);
        user.setLoginIp(localAddr);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        //对管理员密码进行加密
        String password = user.getPassword();
        //加密
        String encode = bCryptPasswordEncoder.encode(password);
        user.setPassword(encode);

        System.out.println("===================================>>>>>>>" + user);
        //保存到数据库
        userMapper.insert(user);

        //更新设置表的，添加标记
        TbSettings tbSettings = new TbSettings();
        tbSettings.setId(String.valueOf(idWorker.nextId()));
        tbSettings.setCreateTime(new Date());
        tbSettings.setUpdateTime(new Date());
        tbSettings.setKey(Constrants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        tbSettings.setValue("1");
        System.out.println("=============================>>>>>>settings===" + tbSettings);
        settingsMapper.insertinto(tbSettings);

        return ResponseResult.success("注册管理账户成功！");
    }

    /*
     * 发送图灵验证码
     * */
    @Override
    public void createCaptcha(HttpServletResponse response, HttpServletRequest request) throws Exception {
//        if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < 13) {
//            return;
//        }
        //防止重复创建，占用redis的太多资源，检查上一次的id，如果有的话重复利用
        String lastId = CookieUtils.getCookie(request, Constrants.User.LAST_CAPTCHA_ID);
        String key;
        if (TextUtils.isEmpty(lastId)) {
            key = idWorker.nextId() + "";
        } else {
            key = lastId;
        }
        try {
//            key = Long.parseLong(captchaKey);
            //处理
            // 设置请求头为输出图片类型
            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            // 三个参数分别为宽、高、位数
            SpecCaptcha specCaptcha = new SpecCaptcha(200, 50, 5);
            //随机字体
            int fontIndex = random.nextInt(CAPTCHA_FONT_TYPES.length);
            log.info("================index===>>>" + fontIndex);
            // 设置字体
            specCaptcha.setFont(fontIndex/*new Font("Captcha.FONT_1", Font.PLAIN, 32)*/);  // 有默认字体，可以不用设置

            int typeIndex = random.nextInt(CAPTCHA_TYPE.length);
            // 设置类型，纯数字、纯字母、字母数字混合
            specCaptcha.setCharType(typeIndex);
            String content = specCaptcha.text().toLowerCase();
            log.info("=============================>>>>>>" + content);
            // 验证码存入session
//        request.getSession().setAttribute("captcha", specCaptcha.text().toLowerCase());
//          保存到redis
            //把这个id写道cookie里面，用于查询验证码的正确性
            CookieUtils.setUpCookie(response, Constrants.User.LAST_CAPTCHA_ID, key);
            redisUtil.set(Constrants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);
            // 输出图片流
            specCaptcha.out(response.getOutputStream());

        } catch (Exception e) {
            return;
        }

    }

    /*
    场景，注册，找回密码，修改密码，修改邮箱，
    注册（register）：已被注册给出提示
    找回密码（forget）：没有注册提示暂无注册
    修改邮箱(update)：新的邮箱已被注册提示

    * 发送邮件验证码
    * */
    @Override
    public ResponseResult sendEmail(String type, HttpServletRequest request, String emailAddress, String captchaCode) {
        //检查人类验证码是否正确
        //从cookie里面拿到key
        String captchaId = CookieUtils.getCookie(request, Constrants.User.LAST_CAPTCHA_ID);
        log.info("captchaId:::=====>>>"+captchaId);
        String captcha = (String) redisUtil.get(Constrants.User.KEY_CAPTCHA_CONTENT+captchaId);
        log.info("captcha:::=====>>>"+captcha);
        if (captcha!=null){
            if (!captcha.equals(captchaCode)) {
                return ResponseResult.failed("人类验证码不正确！");
            }
        }else {
            return ResponseResult.failed("人类验证码错误，或者过期");
        }

        if (emailAddress == null) {
            return ResponseResult.failed("邮箱地址不可以为空！");
        }
        //根据类型查询邮箱是否存在
        if ("register".equals(type) || "update".equals(type)) {
            QueryWrapper<TbUser> emailWrapper = new QueryWrapper<>();
            emailWrapper.eq("email", emailAddress);
            TbUser userEmail = userMapper.selectOne(emailWrapper);
            if (userEmail != null) {
                return ResponseResult.failed("该邮箱已经被注册");
            }
        } else if ("forget".equals(type)) {
//            QueryWrapper<TbUser> emailWrapper = new QueryWrapper<>();
//            emailWrapper.eq("email", emailAddress);
//            TbUser userEmail = userMapper.selectOne(emailWrapper);
//            if (userEmail != null) {
//                return ResponseResult.failed("该邮箱已经被注册");
//            }
        }
        //1、防止暴力发送，同一邮箱频率要查过30s，同一ip最多10次，短信3次
        String remoteAddr = request.getRemoteAddr();//ip
        if (remoteAddr != null) {
            remoteAddr.replaceAll(":", "_");
        }
        log.info("remoteAddress========>>>>>>" + remoteAddr);
        //拿出来，没有就过，没有就判断次数
        //ip请求次数
        Integer ipSendTime = (Integer) redisUtil.get(Constrants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        if (ipSendTime != null && ipSendTime > 10) {

            return ResponseResult.failed("请不要频繁发送！");
        }
        //邮件发送次数
        Object addressSendTime = redisUtil.get(Constrants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (addressSendTime != null) {
            return ResponseResult.failed("邮件发送频繁！");
        }
        //2、检查邮箱地址是否正确
        boolean b = TextUtils.isEmailAddress(emailAddress);
        if (!b) {
            return ResponseResult.failed("邮箱格式错误");
        }

        //3、发送,随机六位数
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000 + code;
        }
        log.info("Email=======>>>>>>>>" + code);
        taskService.sendEmailTaskCode(code + "", emailAddress);//异步调用发送邮件
        //4、保存 ip adress code到 redis
        if (ipSendTime == null) {
            ipSendTime = 0;
        }
        ipSendTime++;

        redisUtil.set(Constrants.User.KEY_EMAIL_SEND_IP + remoteAddr, ipSendTime, 60 * 60);//ip
        redisUtil.set(Constrants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);//email
        redisUtil.set(Constrants.User.KEY_CODE_CONTENT + emailAddress, code + "", 10 * 60);//code


        return ResponseResult.success("邮箱验证码发送成功！");
    }

    /*
        用户注册
    * */
    @Override
    public ResponseResult register(TbUser user, String emailCode, String captchaCode, HttpServletRequest request) {
        if (TextUtils.isEmpty(user.getUserName())) {
            return ResponseResult.failed("用户名不能为空");
        }
        if (TextUtils.isEmpty(user.getEmail())) {
            return ResponseResult.failed("邮箱地址不能为空！");
        }
        //1、当前用户名是否注册
        QueryWrapper<TbUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_name", user.getUserName());
        TbUser username = userMapper.selectOne(userWrapper);
        if (username != null) {
            return ResponseResult.failed("该用户已注册！");
        }
        //2、检查邮箱格式是否正确
        if (!TextUtils.isEmailAddress(user.getEmail())) {
            return ResponseResult.failed("邮箱格式不正确！");
        }
        //3、检查邮箱是否注册
        QueryWrapper<TbUser> emailWrapper = new QueryWrapper<>();
        emailWrapper.eq("email", user.getEmail());
        TbUser email = userMapper.selectOne(emailWrapper);
        if (email != null) {
            return ResponseResult.failed("改邮箱已被注册！");
        }

        //4、检查邮箱验证码是否正确
        //判断过期
        log.info("====>>>>>>>>>>>>>>>邮箱地址：：：" + user.getEmail());
        String emailVeriFyCode = (String) redisUtil.get(Constrants.User.KEY_CODE_CONTENT + user.getEmail());
        if (TextUtils.isEmpty(emailVeriFyCode)) {
            return ResponseResult.failed("验证码无效！");
        }
        //比对验证码
        if (!emailVeriFyCode.equals(emailCode)) {
            return ResponseResult.failed("邮箱验证码不正确");
        } else {
            //redis清除验证码
            redisUtil.del(Constrants.User.KEY_CODE_CONTENT + email);
        }
//        5、检查图灵验证码是否正确
        //从redis里面拿
        String captcha_key = CookieUtils.getCookie(request, Constrants.User.LAST_CAPTCHA_ID);
        if (TextUtils.isEmpty(captcha_key)) {
            return ResponseResult.failed("请允许保留cookie信息");
        }
        String captchaVerifyCode = (String) redisUtil.get(Constrants.User.KEY_CAPTCHA_CONTENT + captcha_key);
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.failed("人类验证码过期！");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.failed("邮箱验证码不正确");
        } else {
            //redis清除验证码
            redisUtil.del(Constrants.User.KEY_CODE_CONTENT + user.getEmail());
        }

        //6、达到注册条件
        //7、对密码加密
        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.failed("密码不能为空！");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //8、补全数据   id,注册ip，登录ip，角色，头像创建/更新时间。
        String remoteAddr = request.getRemoteAddr();
        user.setId(idWorker.nextId() + "");
        user.setRegIp(remoteAddr);
        user.setLoginIp(remoteAddr);
        user.setState("1");
        user.setAvatar(Constrants.User.DEFAULT_AVATAR);
        user.setRoles(Constrants.User.ROLE_NORMAL);
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        //9、保存
        userMapper.insert(user);

        return ResponseResult.success("注册成功！");
    }

    /*
     * 用户登录
     * */
    @Override
    public ResponseResult doLogin(String captcha, TbUser user) {
        HttpServletRequest request = ResquestAndResponse.getRequest();
        HttpServletResponse response = ResquestAndResponse.getResponse();
        String captchaKey = CookieUtils.getCookie(request, Constrants.User.LAST_CAPTCHA_ID);
        String capchaValue = (String) redisUtil.get(Constrants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtils.isEmpty(capchaValue)) {
            return ResponseResult.failed("验证码过期");
        }
        if (!captcha.equals(capchaValue)) {
            return ResponseResult.failed("人类验证码不正确！");
        }
        //1、用户登录，有可能，输入账号，邮箱
        String userName = user.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.failed("账号不能为空！");
        }
        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.failed("密码不能为空！");
        }

        QueryWrapper<TbUser> wrapperUserName = new QueryWrapper<>();
        wrapperUserName.eq("user_name", userName);

        QueryWrapper<TbUser> wrapperUserEmail = new QueryWrapper<>();
        wrapperUserEmail.eq("email", userName);
        //根据用户名查询记录
        TbUser userFromDb = userMapper.selectOne(wrapperUserName);
        log.info("=============>>>userName：：" + userFromDb);
        //查询不到就换成邮箱去查询
        if (userFromDb == null) {
            userFromDb = userMapper.selectOne(wrapperUserEmail);
        }
        //还是为空则提示
        if (userFromDb == null) {
            return ResponseResult.failed("用户名或密码错误！");
        }
        //用户存在，对比密码，输入密码和数据库加密密码对比
        boolean matches = bCryptPasswordEncoder.matches(password, userFromDb.getPassword());
        if (!matches) {
            return ResponseResult.failed("用户名或密码不正确！");
        }
        //判断用户状态，正常则返回
        if (!"1".equals(userFromDb.getState())) {
            return ResponseResult.failed("当前账号已被冻结！");
        }
        //修改更新时间和登录ip
        userFromDb.setLoginIp(request.getRemoteAddr());
        userFromDb.setUpdateTime(new Date());
        createToken(response, userFromDb);
        //删除redis的图形掩码
        redisUtil.del(Constrants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        return ResponseResult.success("登录成功！");
    }

    private String createToken(HttpServletResponse response, TbUser userFromDb) {


       /* //根据来源删除refreshtoken中对应的token_key
        if (Constrants.FROM_MOBILE.equals(from)){

            refreshTokenMapper.deleteMobileTokenKey(tokenKey);
        }else if (Constrants.FROM_PC.equals(from)){
            refreshTokenMapper.deletePcTokenKey(tokenKey);
        }*/

        QueryWrapper<TbRefreshToken> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userFromDb.getId());
        int delete = refreshTokenMapper.delete(wrapper);
        log.info("===delete====" + delete);
        //密码正确，生成token
        String token = JwtTokenUtils.createToken(userFromDb, 1);
        log.info("===============>>>tokenUser==:" + token);

//        String jsonWebToken = JwtUtils.createJsonWebToken(tokenUser);
//        log.info("===============>>>tokenUser==:"+jsonWebToken);
        //返回token的MD5，token会保存到redis
        //前端访问的时候携带token的key，从redis获取即可
//        String tokenKey = DigestUtils.md5DigestAsHex(jsonWebToken.getBytes());
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存到redis，有效期2小时，key是tokenKey
//        redisUtil.set(Constrants.User.KEY_TOKEN+tokenKey,jsonWebToken,60*60*2);
        redisUtil.set(Constrants.User.KEY_TOKEN + tokenKey, token, 60 * 60 * 2);

        //把tokenKye写入cookies
        //从request动态获取
        CookieUtils.setUpCookie(response, Constrants.User.COOKIE_TOKE_KEY, tokenKey);

        //生成refreshToken
        String refreshTokenValue = JwtUtils.createRefreshToken(userFromDb.getId());
        //保存到数据库  refreshToken
        TbRefreshToken refreshToken = new TbRefreshToken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setUserId(userFromDb.getId());
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());

        refreshTokenMapper.insert(refreshToken);
        return tokenKey;
    }

    /*
     * 获取用户信息
     * */
    @Override
    public ResponseResult getUserInfo(String userId) {
        //从数据库获取
        TbUser user = userMapper.selectById(userId);
        //判断是否存在
        if (user == null) {
            return ResponseResult.failed("用户不存在！");
        }

        //存在复制对象，去除隐私信息，清空密码，email，登录ip、注册ip
        user.setPassword("");
        user.setEmail("");
        user.setLoginIp("");
        user.setRegIp("");
        return ResponseResult.success("获取用户信息成功！").setData(user);
    }

    /*
     * 检查email是否存被注册
     * */
    @Override
    public ResponseResult checkEmail(String email) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("email", email);
        TbUser user = userMapper.selectOne(wrapper);
        return user == null ? ResponseResult.failed("该邮箱未注册！") : ResponseResult.success("改邮箱已注册！");
    }

    /*
     * 检查用户名是否被注册
     * */
    @Override
    public ResponseResult checkUsername(String userName) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name", userName);
        TbUser user = userMapper.selectOne(wrapper);
        return user == null ? ResponseResult.failed("该用户未注册！") : ResponseResult.success("用户已注册！");
    }

    /*
     * 更新用户信息
     * */
    @Override
    public ResponseResult updateUserInfo(HttpServletRequest request
            , HttpServletResponse response
            , String userId
            , TbUser user) {
        /*
         * 从token中解析出来的user为了校验权限
         * 只有用户才可以修改自己的信息
         * */
        TbUser userFromKey = checkUser(request, response);
        TbUser userFromDb = userMapper.selectById(userFromKey.getId());

        if (userFromDb == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN("账号未登录");
        }
        //判断id是否一致
        if (!userFromDb.getId().equals(userId)) {
            return ResponseResult.PERMISSION_FORBID("无权限修改");
        }
        //可修改
        //头像
        if (!TextUtils.isEmpty(user.getAvatar())) {
            userFromDb.setAvatar(user.getAvatar());
        }
        //用户名，可以为空
        String userName = user.getUserName();
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", userName);
        //传递进来的userName不为空就根据该userName查询数据库是否重复
        if (!TextUtils.isEmpty(userName) && !userName.equals(userFromKey.getUserName())) {

            TbUser userByUserName = userMapper.selectOne(wrapper);
            if (userByUserName != null) {
                return ResponseResult.failed("该用户名已被注册");
            }
            userFromDb.setUserName(userName);
        }
        //签名
        userFromDb.setSign(user.getSign());
        userFromDb.setUpdateTime(new Date());
        //持久化到数据库
//        userMapper.update(userAccount,null);
        userMapper.updateById(userFromDb);
        //清除redis里面的token，下一次请求，需要解析token时，会根据当前的用户，创建refresh token
        String tokenKey = CookieUtils.getCookie(request, Constrants.User.COOKIE_TOKE_KEY);
        redisUtil.del(Constrants.User.KEY_TOKEN + tokenKey);
        return ResponseResult.success("用户信息修改成功！");

    }

    /*
     * 本质，检查用户是否有登录
     * 登录返回用户信息（通过携带的tokenkey）
     * */
    @Override
    public TbUser checkUser(HttpServletRequest request, HttpServletResponse response) {
        //拿到token——key
        String tokenKey = CookieUtils.getCookie(request, Constrants.User.COOKIE_TOKE_KEY);
        TbUser tbUser = parseByTokenKey(tokenKey);
        if (tbUser == null) {
            //解析出错，mysql查询refreshToken
            QueryWrapper<TbRefreshToken> wrapper = new QueryWrapper<>();
            wrapper.eq("token_key", tokenKey);
            TbRefreshToken refreshToken = refreshTokenMapper.selectOne(wrapper);
            //不存在，当前访问没有登录，提示登录
            if (refreshToken == null) {
                return null;
            }
            try {
                //存在则解析//refreshToken有效创建新的token和新的refresh
                TbUser user = JwtTokenUtils.checkToken(refreshToken.getRefreshToken());
                String userId = refreshToken.getUserId();
                TbUser userFromDb = userMapper.selectById(userId);
                userFromDb.setPassword("");//密码不参与token
                //删掉refreshToken的记录
                refreshTokenMapper.deleteById(refreshToken.getId());
                String newTokenKey = createToken(response, userFromDb);
                return parseByTokenKey(newTokenKey);
            } catch (Exception e1) {
                //refreshToken过期，提示用户登录
                return null;
            }
        }
        return tbUser;
    }

    public TbUser parseByTokenKey(String tokenKey) {
        String token = (String) redisUtil.get(Constrants.User.KEY_TOKEN + tokenKey);
        if (token != null) {
            try {
                TbUser user = JwtTokenUtils.checkToken(token);
                return user;
            } catch (Exception e) {
                return null;
            }
        }
        return null;

    }

    /*
     * 根据id删除用户信息
     * */
    @Override
    public ResponseResult deleteUserById(String userId, HttpServletRequest request, HttpServletResponse response) {
        TbUser currentUser = checkUser(request, response);
        if (currentUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN("用户未登录！");
        }
        TbUser userById = userMapper.selectById(currentUser.getId());

        //判断角色
        if (!Constrants.User.ROLE_ADMIN.equals(userById.getRoles())) {
            return ResponseResult.PERMISSION_FORBID("无权限操作");
        }
        //判断删除的用户是否存在
        TbUser user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseResult.failed("删除的用户不存在！");
        }
        userMapper.deleteById(userId);
        return ResponseResult.success("删除成功！");
    }

    /*
     * 查询用户列表
     * 需要管理员权限
     * */
    @Override
    public ResponseResult getListUser(int page, int size, String userName, String email, HttpServletRequest request, HttpServletResponse response) {

        //判断页码是否是-数
        int checkPage = CheckPageSize.checkPage(page);
        int checkSize = CheckPageSize.checkSize(size);
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        if (!TextUtils.isEmpty(userName)) {
            wrapper.like("user_name", userName);
        }
        if (!TextUtils.isEmpty(email)) {
            wrapper.like("email", email);
        }
        //分页查询
        Page<TbUser> pageList = new Page<>(checkPage, checkSize);
        IPage<TbUser> userIPage = userMapper.selectPage(pageList, wrapper);

        return ResponseResult.success("获取用户列表成功！").setData(userIPage);
    }

    /*
     * 修改密码
     * 检查邮箱
     * */
    @Override
    public ResponseResult updateUserPassword(String verifyCode, TbUser user) {
        String email = user.getEmail();
        //检查是否填写邮箱
        if (email.isEmpty()) {
            return ResponseResult.failed("邮箱地址不能位空");
        }
        //根据邮箱从redis拿验证码
        String redisVerifyCode = (String) redisUtil.get(Constrants.User.KEY_CODE_CONTENT + email);
        //对比
        if (TextUtils.isEmpty(redisVerifyCode) || !redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.failed("验证码错误");
        }

        userMapper.updatePasswordByEmail(bCryptPasswordEncoder.encode(user.getPassword()), user.getEmail());
        redisUtil.del(Constrants.User.KEY_CODE_CONTENT + email);
        return ResponseResult.success("密码更新成功");
    }

    /*
     * 修改邮箱地址
     * 需登录
     * */
    @Override
    public ResponseResult updateEmail(String email, String verifyCode, HttpServletRequest request, HttpServletResponse response) {

        TbUser checkUser = checkUser(request, response);
//        String tokenKey = CookieUtils.getCookie(request(), Constrants.User.COOKIE_TOKE_KEY);
        //拿到tokenKey
        if (checkUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN("用户未登录");
        }

        //2、对比验证码，确保新邮箱是当前用户的
        String verifyEmailCode = (String) redisUtil.get(Constrants.User.KEY_CODE_CONTENT + email);
        if (verifyEmailCode.isEmpty()) {
            return ResponseResult.failed("验证码不正确");
        }

        //删除验证码
        redisUtil.del(Constrants.User.KEY_CODE_CONTENT + email);

        //3、可以修改
        userMapper.updateEmailById(email, checkUser.getId());

        return ResponseResult.success("邮箱修改成功！");
    }

    @Override
    public ResponseResult doLogout() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        String tokenKey = CookieUtils.getCookie(request, Constrants.User.COOKIE_TOKE_KEY);
        if (tokenKey == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN("用户未登录");
        }
        //删除redis
        redisUtil.del(Constrants.User.KEY_TOKEN + tokenKey);
        //删除mysqll
        QueryWrapper<TbRefreshToken> wrapper = new QueryWrapper<>();
        wrapper.eq("token_key", tokenKey);
        refreshTokenMapper.delete(wrapper);
        return ResponseResult.success("退出登录成功！");
    }

    /*
     * 根据token检查用户
     * */
    @Override
    public ResponseResult parseToken(HttpServletRequest request, HttpServletResponse response) {

        TbUser checkUser = checkUser(request, response);
        if (checkUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN("用户未登录!");
        }
        return ResponseResult.success("获取用户成功").setData(checkUser);
    }

    //密码重置
    @Override
    public ResponseResult resetPassword(String userId, String password) {
        //查询出用户
        TbUser user = userMapper.selectById(userId);
        //判断是否存在
        if (user == null) {
            return ResponseResult.failed("用户不存在");
        }
        //密码加密
        user.setPassword(bCryptPasswordEncoder.encode(password));
        //处理结果
        userMapper.updateById(user);
        return ResponseResult.success("密码重置成功！");
    }

    /*
    *
    * */
    @Override
    public ResponseResult checkEmailCode(String emailAddress, String emailCode
            , String captchaCode,HttpServletRequest request) {
        //检查验证码,从cookie中拿到captchaID通过id从redis中拿到验证码
        String captchaId = CookieUtils.getCookie(request, Constrants.User.LAST_CAPTCHA_ID);

        String captcha = (String) redisUtil.get(Constrants.User.KEY_CAPTCHA_CONTENT + captchaId);
        log.info("====<><><>"+captcha);
        if (!captcha.equals(captchaCode)){
            return ResponseResult.failed("人类验证码不正确");
        }
        //检查邮箱code,拿到邮箱验证码
        String redisVerifyCode = (String) redisUtil.get(Constrants.User.KEY_CODE_CONTENT + emailAddress);
        if (!redisVerifyCode.equals(emailCode)){
            return ResponseResult.failed("邮箱验证码不正确");
        }
        return ResponseResult.success("验证码正确");
    }


}
