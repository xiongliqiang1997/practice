package com.qiang.practice.utils;

import java.math.BigDecimal;

/**
 * @Author: CLQ
 * @Date: 2019/8/28
 * @Description: 数学运算工具类
 */
public class MathUtil {
    /**
     * 提供精确的乘法运算。
     * @param num1 被乘数
     * @param num2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal multiply(BigDecimal num1,Integer num2){
        return num1.multiply(new BigDecimal(Integer.toString(num2)));
    }
}
