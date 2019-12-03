package com.qiang.practice.exception;

import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.response.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 捕捉异常
 */
@Component
@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = TokenExpiredException.class)
    public void tokenExpiredExceptionHandler(RuntimeException e) {
        try {
            HttpContextUtils.getHttpServletResponse().sendError(HttpStatus.UNAUTHORIZED.value(), "token已失效");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 捕获异常
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public R IllegalExceptionHandle(IllegalException e){
        logger.error(e.toString());
        return R.error(e.getMessage());
    }

    /**
     * 捕获没有被其他地方捕获的异常
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public R exceptionHandle(RuntimeException e){
        logger.error(e.toString());
        return R.errorCatch();
    }

}
