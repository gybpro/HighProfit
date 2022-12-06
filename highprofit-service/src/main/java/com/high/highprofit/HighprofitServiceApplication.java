package com.high.highprofit;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.high.highprofit.mapper")
@EnableDubbo // 启动dubbo
@EnableScheduling // 启动定时任务
public class HighprofitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighprofitServiceApplication.class, args);
        // ConfigurableApplicationContext applicationContext = SpringApplication.run(HighprofitServiceApplication.class, args);
        // IncomeService incomeService = applicationContext.getBean(IncomeService.class);
        // incomeService.returnMoney();
    }

}
