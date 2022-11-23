package com.high.highprofit;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.high.highprofit.mapper")
@EnableDubbo // 启动dubbo
public class HighprofitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighprofitServiceApplication.class, args);
    }

}
