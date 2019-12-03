package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.ProductInboundRecordMapper;
import com.qiang.practice.model.ProductInboundRecordDetail;
import com.qiang.practice.model.vo.ProductInboundRecordAndDetailVO;
import com.qiang.practice.utils.ExcelUtil;
import com.qiang.practice.utils.FileUtil;
import com.qiang.practice.utils.UUIDUtil;
import com.qiang.practice.utils.response.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/30
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class ProductInboundRecordServiceImpl implements ProductInboundRecordService {
    @Autowired
    private ProductInboundRecordMapper productInboundRecordMapper;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //条件查询
        List<ProductInboundRecordAndDetailVO> orgPageInfo = productInboundRecordMapper.getListByCondition(paramMap);
        PageInfo<ProductInboundRecordAndDetailVO> pageInfo = new PageInfo<>(orgPageInfo);
        return R.ok(pageInfo);
    }

    @Override
    public R getById(Long id) {
        //根据id获取入库详情(包括商品名称和第一张商品图片)
        ProductInboundRecordAndDetailVO productInboundRecordAndDetailVO = productInboundRecordMapper.getById(id);
        for (ProductInboundRecordDetail productInboundRecordDetail :
                productInboundRecordAndDetailVO.getProductInboundRecordDetailList()) {
            if (productInboundRecordDetail.getFirstProductImgPath() != null) {
                //图片url拼串
                productInboundRecordDetail.setFirstProductImgPath(
                        serverConfig.getImageUrl(productInboundRecordDetail.getFirstProductImgPath()));
            }
        }
        return R.ok(productInboundRecordAndDetailVO);
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {
        Long start = System.currentTimeMillis();
        //获取所有日志列表
        List<ProductInboundRecordAndDetailVO> inboundRecordDetailList = productInboundRecordMapper.getList();
        //创建商品入库Excel
        HSSFWorkbook workbook = ExcelUtil.createInboundRecordExcel("商品入库记录表", "商品入库记录表", 7, inboundRecordDetailList);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //清空缓存
            response.reset();
            //定义浏览器响应表头，顺带定义下载名(中文名需要转义)
            response.setHeader("Content-disposition", "attachment;filename="
                    +  URLEncoder.encode(UUIDUtil.getUUID() + ".xls", "UTF-8"));
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
}
