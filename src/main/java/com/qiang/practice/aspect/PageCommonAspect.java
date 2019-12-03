package com.qiang.practice.aspect;

import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.exception.IllegalException;
import com.qiang.practice.utils.response.RUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/29
 * @Description: TODO
 */
@Aspect
@Component
public class PageCommonAspect {

    @Pointcut("@annotation(com.qiang.practice.annotation.PageCommon)")
    public void pagePointCut() {

    }

    @Around("pagePointCut()")
    public Object aroundPage(ProceedingJoinPoint joinPoint) {
        //获取参数(paramMap)
        Object[] objects = joinPoint.getArgs();
        if (objects.length == 0) {
            //没携带任何参数
            throw new IllegalException(TipsConstants.PARAM_NOT_ENOUGH);
        } else {
            Map<String, Object> paramMap = (HashMap<String, Object>) objects[0];
            if (RUtil.isPageParamAbsent(paramMap)) {
                //没携带分页所必需的参数
                throw new IllegalException(TipsConstants.PARAM_NOT_ENOUGH);
            } else {
                try {
                    //若没携带page参数，则将其设置为第一页
                    paramMap.putIfAbsent("page", 1);
                    //设置完后将参数放进接口
                    objects[0] = paramMap;
                    return joinPoint.proceed(objects);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }
        }
    }

}
