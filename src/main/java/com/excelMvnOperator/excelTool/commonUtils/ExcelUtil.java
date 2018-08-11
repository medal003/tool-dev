/**
 * Copyright by meideng.zh (c) 2018 Apple Inc.
 * Date:2018年7月14日下午6:23:56
 * excelTool
 * com.excelMvnOperator.excelTool.commonUtils
 * TODO
 */
package com.excelMvnOperator.excelTool.commonUtils;

/**  
 * ClassName: ExcelUtil.java <br/>  
 * Function:  
 * @author meideng.zh
 * @date: 2018年7月14日 下午6:23:56
 * @version   
 * @since JDK 1.8  
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;


/**
 * excel工具类 提供读取和写入excel的功能
 * 
 * @author JIANGYOUYAO
 * @email 935090232@qq.com
 * @date 2017年12月20日
 */
public class ExcelUtil {

    /**
     * 标题样式
     */
    private final static String STYLE_HEADER = "header";
    /**
     * 表头样式
     */
    private final static String STYLE_TITLE = "title";
    /**
     * 数据样式
     */
    private final static String STYLE_DATA = "data";

    /**
     * 存储样式
     */
    private static final HashMap<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();

    /**
     * 读取excel文件里面的内容 支持日期，数字，字符，函数公式，布尔类型
     * 
     * @author JIANGYOUYAO
     * @email 935090232@qq.com
     * @date 2017年12月20日
     * @param file
     * @param rowCount
     * @param columnCount
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    /**
     * 解析读取excel文件
     * @param file 文件
     * @param rowCount 读取文件的有效行数, 传入null则为文件数据有效行
     * @param columnCount 有效列，同行
     * @param sheetIndex sheet的编号，如果传入null,则读取文件所有sheet页
     * @return 返回List<ExcelSheetPO>类型数据
     * @throws FileNotFoundException
     * @throws IOException
     * meideng.zh  2018年7月14日下午8:25:41
     */
    public static List<ExcelSheetPO> readExcel(File file, Integer rowCount, Integer columnCount,Integer sheetIndex)
            throws FileNotFoundException, IOException {

        // 根据后缀名称判断excel的版本
        String extName = FileCommonUtils.getFileExt(file.getAbsolutePath());
        Workbook wb = null;
        if (ExcelVersion.V2003.getSuffix().equals(extName)) {
            wb = new HSSFWorkbook(new FileInputStream(file));

        } else if (ExcelVersion.V2007.getSuffix().equals(extName)) {
            wb = new XSSFWorkbook(new FileInputStream(file));

        } else {
            // 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
            throw new IllegalArgumentException("Invalid excel version");
        }
        // 开始读取数据
        List<ExcelSheetPO> sheetPOs = new ArrayList<ExcelSheetPO>();
        // 解析sheet
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
        	if (null != sheetIndex && i != sheetIndex ) {
        		continue;
        	}
            Sheet sheet = wb.getSheetAt(i);
            List<List<Object>> dataList = new ArrayList<List<Object>>();
            ExcelSheetPO sheetPO = new ExcelSheetPO();
            sheetPO.setSheetName(sheet.getSheetName());
            sheetPO.setDataList(dataList);
            int readRowCount = 0;
            if (rowCount == null || rowCount > sheet.getPhysicalNumberOfRows()) {
                readRowCount = sheet.getPhysicalNumberOfRows();
            } else {
                readRowCount = rowCount;
            }
            // 解析sheet 的行
            for (int j = sheet.getFirstRowNum(); j < readRowCount; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                if (row.getFirstCellNum() < 0) {
                    continue;
                }
                int readColumnCount = 0;
                if (columnCount == null || columnCount > row.getLastCellNum()) {
                    readColumnCount = (int) row.getLastCellNum();
                } else {
                    readColumnCount = columnCount;
                }
                List<Object> rowValue = new LinkedList<Object>();
                // 解析sheet 的列
                for (int k = 0; k < readColumnCount; k++) {
                    Cell cell = row.getCell(k);
                    rowValue.add(getCellValue(wb, cell));
                }
                dataList.add(rowValue);
            }
            sheetPOs.add(sheetPO);
        }
        return sheetPOs;
    }

    private static Object getCellValue(Workbook wb, Cell cell) {
        Object columnValue = null;
        if (cell != null) {
            DecimalFormat df = new DecimalFormat("0");// 格式化 number
            // String
            // 字符
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
            DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                columnValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                    columnValue = df.format(cell.getNumericCellValue());
                } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    columnValue = nf.format(cell.getNumericCellValue());
                } else {
                    columnValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                columnValue = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                columnValue = "";
                break;
            case Cell.CELL_TYPE_FORMULA:
                // 格式单元格
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                evaluator.evaluateFormulaCell(cell);
                CellValue cellValue = evaluator.evaluate(cell);
                columnValue = cellValue.getNumberValue();
                break;
            default:
                columnValue = cell.toString();
            }
        }
        return columnValue;
    }

    /**
     * 在硬盘上写入excel文件
     * 
     * @author JIANGYOUYAO
     * @email 935090232@qq.com
     * @date 2017年12月20日
     * @param version
     * @param excelSheets
     * @param filePath
     * @throws IOException
     */
    public static void createWorkbookAtDisk(ExcelVersion version, List<ExcelSheetPO> excelSheets, String filePath)
            throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filePath);
        createWorkbookAtOutStream(version, excelSheets, fileOut, true);
    }

    /**
     * 把excel表格写入输出流中，输出流会被关闭
     * 
     * @author JIANGYOUYAO
     * @email 935090232@qq.com
     * @date 2017年12月20日
     * @param version
     * @param excelSheets
     * @param outStream
     * @param closeStream
     *            是否关闭输出流
     * @throws IOException
     */
    public static void createWorkbookAtOutStream(ExcelVersion version, List<ExcelSheetPO> excelSheets,
            OutputStream outStream, boolean closeStream) throws IOException {
        if (!CollectionUtils.isEmpty(excelSheets)) {
            Workbook wb = createWorkBook(version, excelSheets);
            wb.write(outStream);
            if (closeStream) {
                outStream.close();
            }
        }
    }

    private static Workbook createWorkBook(ExcelVersion version, List<ExcelSheetPO> excelSheets) {
        Workbook wb = createWorkbook(version);
        for (int i = 0; i < excelSheets.size(); i++) {
            ExcelSheetPO excelSheetPO = excelSheets.get(i);
            if (excelSheetPO.getSheetName() == null) {
                excelSheetPO.setSheetName("sheet" + i);
            }
            // 过滤特殊字符
            Sheet tempSheet = wb.createSheet(WorkbookUtil.createSafeSheetName(excelSheetPO.getSheetName()));
            buildSheetData(wb, tempSheet, excelSheetPO, version);
        }
        return wb;
    }

    private static void buildSheetData(Workbook wb, Sheet sheet, ExcelSheetPO excelSheetPO, ExcelVersion version) {
        sheet.setDefaultRowHeight((short) 400);
        sheet.setDefaultColumnWidth((short) 10);
        createTitle(sheet, excelSheetPO, wb, version);
        createHeader(sheet, excelSheetPO, wb, version);
        createBody(sheet, excelSheetPO, wb, version);
    }

    private static void createBody(Sheet sheet, ExcelSheetPO excelSheetPO, Workbook wb, ExcelVersion version) {
        List<List<Object>> dataList = excelSheetPO.getDataList();
        for (int i = 0; i < dataList.size() && i < version.getMaxRow(); i++) {
            List<Object> values = dataList.get(i);
            Row row = sheet.createRow(2 + i);
            for (int j = 0; j < values.size() && j < version.getMaxColumn(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(getStyle(STYLE_DATA, wb));
                cell.setCellValue(values.get(j).toString());
            }
        }

    }

    private static void createHeader(Sheet sheet, ExcelSheetPO excelSheetPO, Workbook wb, ExcelVersion version) {
        String[] headers = excelSheetPO.getHeaders();
        Row row = sheet.createRow(1);
        for (int i = 0; i < headers.length && i < version.getMaxColumn(); i++) {
            Cell cellHeader = row.createCell(i);
            cellHeader.setCellStyle(getStyle(STYLE_HEADER, wb));
            cellHeader.setCellValue(headers[i]);
        }

    }

    private static void createTitle(Sheet sheet, ExcelSheetPO excelSheetPO, Workbook wb, ExcelVersion version) {
        Row titleRow = sheet.createRow(0);
        Cell titleCel = titleRow.createCell(0);
        titleCel.setCellValue(excelSheetPO.getTitle());
        titleCel.setCellStyle(getStyle(STYLE_TITLE, wb));
        // 限制最大列数
        int column = excelSheetPO.getDataList().size() > version.getMaxColumn() ? version.getMaxColumn()
                : excelSheetPO.getDataList().size();
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, column - 1));
    }

    private static CellStyle getStyle(String type, Workbook wb) {

        if (cellStyleMap.containsKey(type)) {
            return cellStyleMap.get(type);
        }
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setWrapText(true);

        if (STYLE_HEADER == type) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 16);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
        } else if (STYLE_TITLE == type) {
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 18);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style.setFont(font);
        } else if (STYLE_DATA == type) {
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
        }
        cellStyleMap.put(type, style);
        return style;
    }

    private static Workbook createWorkbook(ExcelVersion version) {
        switch (version) {
        case V2003:
            return new HSSFWorkbook();
        case V2007:
            return new XSSFWorkbook();
        }
        return null;
    }
}
