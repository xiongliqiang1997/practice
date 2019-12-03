package com.qiang.practice.exception;

/**
 * @Author: CLQ
 * @Date: 2019/8/8
 * @Description: TODO
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String description) {
        super(description, null);
    }

}
