package com.wwjjbt.sob_blog_system_mp.utils;

public interface Constrants {

    String FROM_PC = "pc";
    String FROM_MOBILE = "mobile";

    interface User{
        String ROLE_ADMIN = "role_admin";
        String DEFAULT_STATE = "1";
        String ROLE_NORMAL = "role_normal";
        String DEFAULT_AVATAR = "https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp";
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        String KEY_CODE_CONTENT = "key_CODE_content_";
        String KEY_EMAIL_SEND_IP = "key_email_send_ip";
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address";
        String KEY_TOKEN = "key_token_";
        String LAST_CAPTCHA_ID = "l_c_i";
        String COOKIE_TOKE_KEY = "blog_token";
    }
    interface Settings{
        String MANAGER_ACCOUNT_INIT_STATE = "MANAGER_ACCOUNT_INIT_STATE";
        String WEB_SIZE_TITLE="web_size_title";
        String WEB_SIZE_DESCRIPTION = "web_size_description";
        String WEB_SIZE_KEYWORDS = "web_size_keywords";
        String WEB_SIZE_VIEW_COUNT = "web_size_view_count";

    }
    interface ImageType{
        String PREFIX = "image/";
        String TYPE_JPG = "jpg";
        String TYPE_PNG = "png";
        String TYPE_GIF = "gif";
        String TYPE_JPG_WITH_PREFIX = PREFIX+"jpg";
        String TYPE_PNG_WITH_PREFIX = PREFIX+"png";
        String TYPE_GIF_WITH_PREFIX = PREFIX+"gif";
    }
    interface Article{
        String KEY_ARTICLE_VIEW_COUNT = "key_article_view_count";
        int TITLE_LENGTH_MAX = 128;
        int SUMMARY_MAX_LENGTH = 256;
        //删除0  1 已发布  2草稿  3置顶
        String STATE_DELETE = "0";
        String STATE_PULISH = "1";
        String STATE_DRAFT = "2";
        String STATE_TOP = "3";
        String TYPE_MD = "1";
    }

    interface Commend{

        //删除0  1 已发布  2草稿  3置顶

        String STATE_PULISH = "1";
        String STATE_TOP = "3";
        String KEY_COMMEND_FIRST_PAGE_CACHE="key_commend_first_page_cache_";

    }

}
