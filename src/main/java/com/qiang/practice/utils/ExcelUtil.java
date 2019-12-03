package com.qiang.practice.utils;

import com.qiang.practice.model.ProductInboundRecordDetail;
import com.qiang.practice.model.vo.ProductInboundRecordAndDetailVO;
import com.qiang.practice.utils.response.StatisticJsonObj;
import com.qiang.practice.utils.response.StatisticsObj;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/7
 * @Description: Excel工具类
 */
@SuppressWarnings("all")
public class ExcelUtil {

    public static HSSFWorkbook createStatisticsExcel(String sheetName, String title, int columnNum, StatisticJsonObj statisticJsonObj) {

        //创建Excel对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet对象
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFFont font = workbook.createFont();
        HSSFCellStyle style = workbook.createCellStyle();
        setFont(font, style, "宋体", false, 11, HorizontalAlignment.CENTER);

        HSSFFont fontBold = workbook.createFont();
        HSSFCellStyle styleBold = workbook.createCellStyle();
        setFont(fontBold, styleBold, "宋体", true, 14, HorizontalAlignment.CENTER);

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 0);

        //填写标题行
        setRow(workbook, sheet, styleBold, fontBold, 0, new String[]{title}, true,
                0, 0, 0, columnNum - 1, false, 0,
                false, 0, false, null, region);
        //填写副标题行
        setRow(workbook, sheet, style, font, 1, ArrayUtils.addAll(new String[]{"板块名称"}, statisticJsonObj.getMonthArray()),
                false, 0, 0, 0, 0, false, 0,
                false, 0, false, null, region);
        String[] data = new String[columnNum];
        int rowNum = 2;
        for (StatisticsObj obj : statisticJsonObj.getList()) {
            data[0] = obj.getName();
            //将Integer类型的数据转为String型，然后添加到本行数组中
            for (int i = 1; i <= columnNum - 1; i++) {
                data[i] = obj.getData()[i - 1].toString();
            }
            for (int i = 1; i <= columnNum - 1; i++) {
                //循环填写每一行
                setRow(workbook, sheet, style, font, rowNum, data, false, 0, 0, 0, 0,
                        false, 0, false, 0,
                        false, null, region);
            }
            rowNum++;

        }
        //设置为根据内容自动调整列宽
        sheet.autoSizeColumn(0);
        return workbook;

    }

    public static HSSFWorkbook createLogsExcel(String sheetName, String title, int columnNum, List<Map<String, Object>> data) {

        //创建Excel对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet对象
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFFont font = workbook.createFont();
        HSSFCellStyle style = workbook.createCellStyle();
        setFont(font, style, "宋体", false, 11, HorizontalAlignment.CENTER);

        HSSFFont fontBold = workbook.createFont();
        HSSFCellStyle styleBold = workbook.createCellStyle();
        setFont(fontBold, styleBold, "宋体", true, 14, HorizontalAlignment.CENTER);

        HSSFFont fontLeft = workbook.createFont();
        HSSFCellStyle styleLeft = workbook.createCellStyle();
        setFont(fontLeft, styleLeft, "宋体", false, 11, HorizontalAlignment.LEFT);

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 0);

        //填写标题行
        setRow(workbook, sheet, styleBold, fontBold, 0, new String[]{title}, true,
                0, 0, 0, columnNum - 1, false, 0,
                false, 0, false, null, region);
        //填写副标题行
        setRow(workbook, sheet, style, font, 1, new String[]{"日期", "操作人", "操作类型", "操作状态", "操作内容"},
                false, 0, 0, 0, 0, false, 0,
                false, 0, false, null, region);

        int index = 2;
        for (Map<String, Object> map : data) {
            String[] dataArray = new String[5];
            dataArray[0] = map.get("createDate").toString();
            dataArray[1] = map.get("userName").toString();
            dataArray[2] = map.get("logType").toString();
            dataArray[3] = map.get("logResult").toString();
            dataArray[4] = map.get("content").toString();
            //循环填写每一行
            setRow(workbook, sheet, styleLeft, fontLeft, index, dataArray, false, 0, 0,
                    0, 0, false, 0, false,
                    0, false, null, region);
            index++;
        }


        //设置为根据内容自动调整列宽
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(4);
        return workbook;
    }

    /**
     * 创建商品入库Excel
     *
     * @param sheetName
     * @param title
     * @param i
     * @param inboundRecordDetailList
     * @return
     */
    public static HSSFWorkbook createInboundRecordExcel(String sheetName, String title, int columnNum, List<ProductInboundRecordAndDetailVO> inboundRecordDetailList) {
        //创建Excel对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet对象
        HSSFSheet sheet = workbook.createSheet(sheetName);
        HSSFFont font = workbook.createFont();
        HSSFCellStyle style = workbook.createCellStyle();
        setFont(font, style, "宋体", false, 11, HorizontalAlignment.CENTER);

        HSSFFont fontBold = workbook.createFont();
        HSSFCellStyle styleBold = workbook.createCellStyle();
        setFont(fontBold, styleBold, "宋体", true, 14, HorizontalAlignment.CENTER);

        HSSFFont fontLeft = workbook.createFont();
        HSSFCellStyle styleLeft = workbook.createCellStyle();
        HSSFCellStyle styleLeftVerticalCenter = workbook.createCellStyle();
        setFont(fontLeft, styleLeft, "宋体", false, 11, HorizontalAlignment.LEFT);
        setFont(fontLeft, styleLeftVerticalCenter, "宋体", false, 11, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 0);

        //填写标题行
        setRow(workbook, sheet, styleBold, fontBold, 0, new String[]{title}, true,
                0, 0, 0, columnNum - 1, false, 0,
                false, 0, false, null, region);
        //填写副标题行
        setRow(workbook, sheet, style, font, 1, new String[]{"日期", "入库人", "入库商品种类", "货源地址", "备注", "商品信息", "入库数量"},
                false, 0, 0, 0, 0, false, 0,
                false, 0, false, null, region);

        int index = 2;
        for (ProductInboundRecordAndDetailVO inboundRecordDetail : inboundRecordDetailList) {
            String[] dataArray = new String[5];
            dataArray[0] = DateUtil.getTimeString(inboundRecordDetail.getCreateDate());
            dataArray[1] = inboundRecordDetail.getUserName();
            dataArray[2] = String.valueOf(inboundRecordDetail.getProductInboundRecordDetailList().size());
            dataArray[3] = inboundRecordDetail.getProductSource();
            dataArray[4] = StringUtils.defaultIfEmpty(inboundRecordDetail.getRemark(), "");
            int subIndex = 0;
            //循环填写每一行
            HSSFRow row = (HSSFRow) setRow(workbook, sheet, styleLeftVerticalCenter, fontLeft, index, dataArray, false, index, index + inboundRecordDetail.getProductInboundRecordDetailList().size() - 1,
                    0, 0, true, 4, false,
                    0, true, null, region);
            for (ProductInboundRecordDetail inboundDetail : inboundRecordDetail.getProductInboundRecordDetailList()) {
                String[] subDataArray = new String[2];
                subDataArray[0] = inboundDetail.getProductName();
                subDataArray[1] = inboundDetail.getNum().toString();
                //循环填写每一行
                setRow(workbook, sheet, styleLeft, fontLeft, index + subIndex, subDataArray, false,
                        0, 0, 0, 0, false, 0,
                        true, 5, false, row, region);
                subIndex++;
            }

            index += inboundRecordDetail.getProductInboundRecordDetailList().size();
        }

        //设置为根据内容自动调整列宽(合并单元格的的单元格必须加true)
        sheet.autoSizeColumn(0, true);
        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(3, true);
        sheet.autoSizeColumn(4, true);
        sheet.autoSizeColumn(5);
        return workbook;
    }

    public static void setFont(HSSFFont font, HSSFCellStyle style, String fontName, boolean bold, int fontSize, HorizontalAlignment alignment) {
        font.setFontName(fontName);  //字体类型
        font.setBold(bold);  //是否加粗
        font.setFontHeightInPoints((short) fontSize);  //字号
        style.setAlignment(alignment);  //样式（如：水平居中/垂直居中等）
        style.setFont(font);
    }

    public static void setFont(HSSFFont font, HSSFCellStyle style, String fontName, boolean bold, int fontSize, HorizontalAlignment alignment, VerticalAlignment verticalAlignment) {
        font.setFontName(fontName);  //字体类型
        font.setBold(bold);  //是否加粗
        font.setFontHeightInPoints((short) fontSize);  //字号
        style.setAlignment(alignment);  //水平样式（如：居中/居左/居右等）
        style.setVerticalAlignment(verticalAlignment); //垂直样式（如：居中/居上/居下等）
        style.setFont(font);
    }

    public static Object setRow(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle style, HSSFFont font, int rowNum, Object[] contentArray,
                              boolean isTitle, int firstRow, int lastRow, int firstCol, int lastCol, boolean isEveryColumnMerge, int maxMergeColumnNum,
                              boolean isSubDataArray, int subDataStartIndex, boolean hasSubData, HSSFRow row, CellRangeAddress region) {
        if (row == null || row.getRowNum() != rowNum) {
            row = sheet.createRow(rowNum);
        }
        for (int i = 0; i < contentArray.length; i++) {
            /**
             * firstRow 区域中第一个单元格的行号
             * lastRow 区域中最后一个单元格的行号
             * firstCol 区域中第一个单元格的列号
             * lastCol 区域中最后一个单元格的列号
             */
            if (isTitle) {
                region.setFirstColumn(firstCol);
                region.setLastColumn(lastCol);
                region.setFirstRow(firstRow);
                region.setLastRow(lastRow);
                sheet.addMergedRegion(region);
            }
            if (isEveryColumnMerge) {
                if (lastRow > firstRow) {
                    region.setFirstColumn(i);
                    region.setLastColumn(i);
                    region.setFirstRow(firstRow);
                    region.setLastRow(lastRow);
                    sheet.addMergedRegion(region);
                }
            }
            if (isSubDataArray) {
                setCell(style, row, i + subDataStartIndex, String.valueOf(contentArray[i]));
            } else {
                setCell(style, row, i, String.valueOf(contentArray[i]));
            }
        }
        if (hasSubData) {
            return row;
        } else {
            return null;
        }
    }

    public static void setCell(HSSFCellStyle style, HSSFRow row, int column, String content) {
        //在这一行的第一个格子创建一个单元格
        HSSFCell cell = row.createCell(column);
        if (content != null) {
            //给这个单元格赋值
            cell.setCellValue(content);
        }
        //将样式设置给单元格
        cell.setCellStyle(style);
    }

}
