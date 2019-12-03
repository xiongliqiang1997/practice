package com.qiang.practice.exception;

/**
 * @Author: CLQ
 * @Date: 2019/8/8
 * @Description: TODO
 */
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String description) {
        super(description);
    }
}
