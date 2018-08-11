package com.excelMvnOperator.excelTool;

import com.excelMvnOperator.excelTool.loadConfig.LoadAllConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * author: meideng.zh
 * date: 2018/7/28 22:27
 * comment:
 */
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private LoadAllConfig remoteProperties;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println(remoteProperties.getExcelFileConfig().getMyfileName());
    }
}
