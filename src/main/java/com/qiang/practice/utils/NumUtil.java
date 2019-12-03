package com.qiang.practice.utils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: CLQ
 * @Date: 2019/7/9
 * @Description: 数字处理工具类
 */
public class NumUtil {

    /**
     * 在当前最大的orderby基础上 +1
     * @param maxOrderby
     * @return
     */
    public static Integer getOrderbyByMaxOrderby(Integer maxOrderby) {
        return maxOrderby == null ? 1 : maxOrderby+1;
    }

    public static Long ifIdNull(Long id) {
        if (null == id) {
            id = 1L;
        }
        return id;
    }

    public static Map<String, Object> convertUserSexToInteger(Map<String, Object> paramMap) {
        if (null != paramMap.get("userSex")) {
            String userSex = paramMap.get("userSex").toString();
            if ("-1".equals(userSex)) {
                paramMap.put("userSex", Integer.valueOf(paramMap.get("userSex").toString()));
            } else if ("男".equals(userSex)){
                paramMap.put("userSex", 0);
            } else if ("女".equals(userSex)){
                paramMap.put("userSex", 1);
            } else if ("保密".equals(userSex)) {
                paramMap.put("userSex", -1);
            }
        }
        return paramMap;
    }

    /**
     * 处理数据，如果该值为空则置为0
     * @param num
     * @return
     */
    public static Long ifNumNullChangeToZero(Long num) {
        return num == null ? 0L : num;
    }

    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
