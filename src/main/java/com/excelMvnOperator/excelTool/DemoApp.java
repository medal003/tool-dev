package com.excelMvnOperator.excelTool;

import com.excelMvnOperator.excelTool.loadConfig.ExcelFileConfig;
import com.excelMvnOperator.excelTool.loadConfig.LoadAllConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

/**  
 * ClassName: App.java <br/>  
 * Function:  
 * @author meideng.zh
 * @date: 2018年7月14日 下午5:29:30
 * @version   
 * @since JDK 1.8  
 */
@RestController
@SpringBootApplication //Spring Boot核心注解，用于开启自动配置
@EnableConfigurationProperties({ExcelFileConfig.class})
//@MapperScan("com.excelMvnOperator.excelTool.**.dao")  // 数据库dao层扫描
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})  //阻止spring boot自动注入dataSource bean,不需要连接数据库时开启配置
@Data
public class DemoApp 
{
	@Autowired
	private LoadAllConfig remoteProperties;

	@RequestMapping("/123")
	String index(){
		System.out.println(remoteProperties.getExcelFileConfig().getMyfileName());
		return "hi spring boot你好+"+remoteProperties.getExcelFileConfig().getMyfileName();
	}
	
	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(DemoApp .class);
		springApplication.addListeners(new ApplicationStartup());
		SpringApplication.run(DemoApp.class, args);
	}
}
