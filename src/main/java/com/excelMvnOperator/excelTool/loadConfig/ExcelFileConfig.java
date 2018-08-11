/**
 * Copyright by meideng.zh (c) 2018 Apple Inc.
 * Date:2018年7月14日下午8:52:33
 * excelTool
 * com.excelMvnOperator.excelTool.loadConfig
 * TODO
 */
package com.excelMvnOperator.excelTool.loadConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**  
 * ClassName: ExcelFileConfig.java <br/>  
 * Function:  读取ExcelFile的相关配置
 * @author meideng.zh
 * @date: 2018年7月14日 下午8:52:33
 * @version   
 * @since JDK 1.8  
 */
@Configuration
@ConfigurationProperties(prefix = "excelFile", ignoreUnknownFields = false)
@PropertySource("classpath:/config/excelFile.properties")
@Data
public class ExcelFileConfig {
	/** 
	 * meideng.zh 2018年7月14日下午8:53:55
	 * myfileName:待处理的文件名
	 */ 
	//@Value("${myfileName}")
	private String myfileName;
}
