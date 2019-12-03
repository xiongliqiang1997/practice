package com.qiang.practice.exception;

/**
 * @Author: CLQ
 * @Date: 2019/8/29
 * @Description: TODO
 */
public class IllegalException extends RuntimeException {
    public IllegalException(String description) {
        super(description, null);
    }
}
