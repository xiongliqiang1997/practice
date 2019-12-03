package com.qiang.practice.utils;

import org.springframework.util.DigestUtils;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: MD5工具类
 */
public class MD5Util {

    //盐，用于混和md5
    private static final String slat = "&%5123**#*&&%%$$#@";
    /**
     * 生成加盐的md5
     * @param str
     * @return
     */
    public static String getSlatMD5(String str) {
        String base = str +"/"+slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 生成自定义盐值的md5
     * @param str
     * @return
     */
    public static String getSlatMD5(String str, String slat) {
        String base = str +"/"+slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    public static String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}
