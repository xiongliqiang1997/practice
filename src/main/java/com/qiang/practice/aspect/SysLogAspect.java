package com.qiang.practice.aspect;

import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.base.Constants;
import com.qiang.practice.model.SysLog;
import com.qiang.practice.service.RedisService;
import com.qiang.practice.service.SysLogService;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.IPUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/8/12
 * @Description: TODO
*/

@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.qiang.practice.annotation.MyLog)")
    public void logPointCut() {

    }

    @AfterReturning("logPointCut()")
    public void afterReturning(JoinPoint point) {
        //操作成功，保存成功日志
        saveSysLog(point, 1, null);
    }

    @AfterThrowing(throwing="ex", pointcut = "logPointCut()")
    public void afterThrowing(JoinPoint point, Throwable ex) {
        //操作失败，保存日志，记录失败原因
        saveSysLog(point, 0, ex);
    }

    private void saveSysLog(JoinPoint joinPoint, int logResult, Throwable ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        MyLog myLog = method.getAnnotation(MyLog.class);

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        if(myLog != null && request.getAttribute("notLog") == null){

            String logType = myLog.value();
            //区分新建和编辑操作
            if (StringUtils.contains(logType, "新建/编辑")) {
                String s = request.getAttribute("logType").toString();
                logType = StringUtils.replace(logType, "新建/编辑", s);
            }

            //设置请求类型
            sysLog.setLogType(logType);

            //设置操作状态  1成功  0失败
            sysLog.setLogResult((byte)logResult);

            sysLog.setCreateDate(new Date());

            String content = null;
            //若为登录或注销请求，设置IP地址
            if (StringUtils.contains(logType, "登录") || StringUtils.contains(logType, "注销")) {
                content = "用户ip：" + IPUtils.getClientIpAddr(request) + "。";
                //设置操作用户
                sysLog.setSysUser((Long) request.getAttribute("sysUserId"));
            } else {
                //设置操作用户
                sysLog.setSysUser(redisService.findByToken(request.getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
            }
            //如果操作失败设置失败原因
            if (logResult == 0) {
                if (content != null) {
                    content += "错误原因：" + ex.getMessage();
                } else {
                    content = "错误原因：" + ex.getMessage();
                }
            }
            //设置操作内容
            sysLog.setContent(content);
            //保存系统日志
            sysLogService.addOne(sysLog);
        }


    }
}
