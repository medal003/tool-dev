/**
 * Copyright by meideng.zh (c) 2018 Apple Inc.
 * Date:2018年7月14日下午8:58:12
 * excelTool
 * com.excelMvnOperator.excelTool.loadConfig
 * TODO
 */
package com.excelMvnOperator.excelTool.loadConfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**  
 * ClassName: loadAllConfig.java <br/>  
 * Function:  加载所有的配置文件
 * @author meideng.zh
 * @date: 2018年7月14日 下午8:58:12
 * @version   
 * @since JDK 1.8  
 */
@Data
@Component
//@EnableConfigurationProperties(ExcelFileConfig.class)
public class LoadAllConfig {
	/**
	 * 读取excel文件的相关配置
	 * meideng.zh 2018年7月14日下午8:58:12
	 */
	@Autowired
	private ExcelFileConfig excelFileConfig;

}
