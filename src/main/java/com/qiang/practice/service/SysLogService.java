package com.qiang.practice.service;

import com.qiang.practice.model.SysLog;
import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Date: 2019/8/12
 * @Description: TODO
 */
public interface SysLogService {
    /**
     * 将日志写进数据库
     * @param sysLog
     */
    void addOne(SysLog sysLog);

    /**
     * 分页日志，若各查询条件不为null，则为 分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPageAndCondition(Map<String, Object> paramMap);

    /**
     * 将日志制成Excel并返回给前端下载
     * @param request
     * @param response
     */
    void downloadExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据id查详情
     * @param id
     * @return
     */
    R getById(Long id);
}
