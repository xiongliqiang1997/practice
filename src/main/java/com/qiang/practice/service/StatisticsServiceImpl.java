package com.qiang.practice.service;

import com.qiang.practice.mapper.PlateInfoMapper;
import com.qiang.practice.mapper.PlateMapper;
import com.qiang.practice.mapper.ReadLogMapper;
import com.qiang.practice.utils.ExcelUtil;
import com.qiang.practice.utils.FileUtil;
import com.qiang.practice.utils.UUIDUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.StatisticJsonObj;
import com.qiang.practice.utils.response.StatisticsObj;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/5
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private PlateMapper plateMapper;
    @Autowired
    private PlateInfoMapper plateInfoMapper;
    @Autowired
    private ReadLogMapper readLogMapper;

    @Override
    public R getPublishCounts(String year) {
        //若是查当前年之前的年份，那必定是十二个月
        int month = 12, flag = 1;
        //首先获取当前是哪一年
        Calendar calendar = Calendar.getInstance();

        //判断是否要查今年的数据
        if (year == null || StringUtils.equals(year, String.valueOf(calendar.get(Calendar.YEAR)))) {
            //是当前年，则获得当前时间的月份，月份从0开始所以结果要加1
            month = calendar.get(Calendar.MONTH) + 1;
            year = String.valueOf(calendar.get(Calendar.YEAR));
        }

        //首先获取当前有哪些板块
        List<Map<String, Object>> plateList = plateMapper.getPlateList();

        //返回参数准备  1.月份数组  2.各板块的数据数组
        List<StatisticsObj> dataList = new ArrayList<>();
        String[] monthArray = new String[month];

        for (Map<String, Object> temp : plateList) {
            StatisticsObj statisticsObj = new StatisticsObj();
            statisticsObj.setName(temp.get("plateName").toString());
            Integer[] data = new Integer[month];
            int totalCount = 0;
            for (int i = 1; i <= month; i++) {
                if (flag == 1) {
                    monthArray[i - 1] = i + "月";
                }
                //查询每个板块每个月的发布数量
                int count = plateInfoMapper.getPublishCountByMonth(Long.valueOf(temp.get("id").toString()),
                        i < 10 ? "0" + i : "" + i,
                        year);
                data[i - 1] = count;
                totalCount += count;
            }
            flag = 2;
            statisticsObj.setData(data);
            statisticsObj.setValue(totalCount);
            dataList.add(statisticsObj);
        }
        StatisticJsonObj statisticJsonObj = new StatisticJsonObj();
        statisticJsonObj.setList(dataList);
        statisticJsonObj.setMonthArray(monthArray);
        return R.ok(statisticJsonObj);
    }

    @Override
    public R getReadCounts(String year) {
        //默认查12个月的数据
        int month = 12, flag = 1;
        //首先拿到时间工具类库
        Calendar calendar = Calendar.getInstance();
        //判断是否要查今年的数据
        if (year == null || StringUtils.equals(year, String.valueOf(calendar.get(Calendar.YEAR)))) {
            //是当前年，则获得当前时间的月份，月份从0开始所以结果要加1
            month = calendar.get(Calendar.MONTH) + 1;
            year = String.valueOf(calendar.get(Calendar.YEAR));
        }
        //首先获取当前有哪些板块
        List<Map<String, Object>> plateList = plateMapper.getPlateList();

        //返回参数准备  1.月份数组  2.各板块的数据数组
        String[] monthArray = new String[month];
        List<StatisticsObj> dataList = new ArrayList<>();

        //循环统计各版块数据
        for (Map<String, Object> plate : plateList) {

            StatisticsObj statisticsObj = new StatisticsObj();
            //将各版块的名字填入
            statisticsObj.setName(plate.get("plateName").toString());
            //各月份数据数组准备
            Integer[] data = new Integer[month];
            //统计各月份数据
            for (int i = 1; i <= month; i++) {
                //将月份数组填充好
                if (flag == 1) {
                    monthArray[i - 1] = i + "月";
                }
                //查询当前板块的每个月的数据
                data[i - 1] = readLogMapper.getReadCountByMonth((Long) (plate.get("id")),
                        i < 10 ? "0" + i : "" + i,
                        year);
            }
            //将标志置为2，下次循环不再修改月份数组
            flag = 2;

            //将查到的该板块各月份数据数组填充
            statisticsObj.setData(data);
            //将该板块数据放入集合
            dataList.add(statisticsObj);
        }

        //最终结果返回
        StatisticJsonObj statisticJsonObj = new StatisticJsonObj();
        statisticJsonObj.setList(dataList);
        statisticJsonObj.setMonthArray(monthArray);

        return R.ok(statisticJsonObj);
    }

    @Override
    public void downloadPublishCountsExcel(HttpServletRequest request, HttpServletResponse response, String year) {

        StatisticJsonObj statisticJsonObj = new StatisticJsonObj();
        R result = getPublishCounts(year);
        if (result.isRes()) {
            statisticJsonObj = (StatisticJsonObj) result.getData();
        }

        //首先拿到时间工具类库
        Calendar calendar = Calendar.getInstance();
        HSSFWorkbook workbook = ExcelUtil.createStatisticsExcel(calendar.get(Calendar.YEAR) + "_发布数量统计",
                "发布数量统计("+calendar.get(Calendar.YEAR)+"年)",
                statisticJsonObj.getMonthArray().length + 1, statisticJsonObj);

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
    public void downloadReadCountsExcel(HttpServletRequest request, HttpServletResponse response, String year) {
        StatisticJsonObj statisticJsonObj = new StatisticJsonObj();
        R result = getReadCounts(year);
        if (result.isRes()) {
            statisticJsonObj = (StatisticJsonObj) result.getData();
        }

        HSSFWorkbook workbook = ExcelUtil.createStatisticsExcel("阅读数量统计", "阅读数量统计",
                statisticJsonObj.getMonthArray().length + 1, statisticJsonObj);

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
            FileUtil.closeOutputStream(out);
            FileUtil.closeHSSFWorkbook(workbook);
        }
    }

}
