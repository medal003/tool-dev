/**
 * Copyright by meideng.zh (c) 2018 Apple Inc.
 * Date:2018年7月14日下午6:54:22
 * excelTool
 * com.excelMvnOperator.excelTool.commonUtils
 * TODO
 */
package com.excelMvnOperator.excelTool.commonUtils;

/**  
 * ClassName: Ts.java <br/>  
 * Function:  
 * @author meideng.zh
 * @date: 2018年7月14日 下午6:54:22
 * @version   
 * @since JDK 1.8  
 */

import java.util.List;

import lombok.Data;

/**
 * 定义表格的数据对象
 * @author JIANGYOUYAO
 * @email 935090232@qq.com
 * @date 2017年12月20日
 */
@Data
public class ExcelSheetPO {

    /**
     * sheet的名称
     */
    private String sheetName;

 
    /**
     * 表格标题
     */
    private String title;

    /**
     * 头部标题集合
     */
    private String[] headers;

    /**
     * 数据集合
     */
    private List<List<Object>> dataList;

    
    
    

}