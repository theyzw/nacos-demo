package com.yk.common.core.consts;

public class Consts {

    /**
     * redis key前缀
     */
    public final static String REDIS_KEY_PRE = "yk";

    /**
     * redis锁前缀
     */
    public final static String REDIS_LOCK_PRE = "yk:lock:";

    /**
     * 验证码key cs:vfcode:{userid}
     */
    public final static String REDIS_VFCODE = "yk:vfcode:%s";

    /**
     * 验证码ipkey "cs:vfcode:ip:{ipaddr}
     */
    public final static String REDIS_VFCODE_IP = "yk:vfcode:ip:%s";



    /**
     * redis缓存
     */
    public static class RedisCache {

        /**
         * redis cache默认过期时间 30天
         */
        public final static Integer TIMEOUT_DEFAULT_DAY = 30;

        /**
         * orgdata
         */
        public final static String ORG_DATA = "orgdata";


    }

}
