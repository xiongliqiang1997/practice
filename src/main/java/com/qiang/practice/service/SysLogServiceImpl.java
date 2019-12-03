package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.mapper.SysLogMapper;
import com.qiang.practice.model.SysLog;
import com.qiang.practice.utils.ExcelUtil;
import com.qiang.practice.utils.FileUtil;
import com.qiang.practice.utils.UUIDUtil;
import com.qiang.practice.utils.response.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @Date: 2019/8/12
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void addOne(SysLog sysLog) {
        sysLogMapper.insert(sysLog);
    }

    @Override
    public R getByPageAndCondition(Map<String, Object> paramMap) {
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据条件查询对应的日志列表
        List<Map<String, Object>> orgPageInfo = sysLogMapper.getListByCondition(paramMap);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(orgPageInfo);
        return R.ok(pageInfo);
    }

    @Override
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        //获取所有日志列表
        List<Map<String, Object>> logList = sysLogMapper.getList();

        HSSFWorkbook workbook = ExcelUtil.createLogsExcel("日志记录表", "日志记录表", 5, logList);

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //清空缓存
            response.reset();
            //定义浏览器响应表头，顺带定义下载名(中文名需要转义)
            response.setHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(UUIDUtil.getUUID() + ".xls", "UTF-8"));
            //定义下载的类型，标明是excel文件
            response.setContentType("application/vnd.ms-excel");
            //这时候把创建好的excel写入到输出流
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //养成好习惯，出门记得随手关门
            FileUtil.closeOutputStream(out);
            FileUtil.closeHSSFWorkbook(workbook);
        }
    }

    @Override
    public R getById(Long id) {
        //根据id查详情
        return R.ok(sysLogMapper.selectByPrimaryKey(id));
    }
}
