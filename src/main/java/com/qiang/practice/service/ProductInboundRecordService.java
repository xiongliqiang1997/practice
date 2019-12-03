package com.qiang.practice.service;

import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/30
 * @Description: TODO
 */
public interface ProductInboundRecordService {
    /**
     * 分页查询入库记录，可以包含查询条件
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 根据id获取入库详情
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 将入库记录导出为Excel
     * @return
     */
    void downloadExcel(HttpServletResponse response);
}
