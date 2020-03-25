package com.kingstar.indicatorservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kingstar.indicatorservice.mapper")
public class IndicatorserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndicatorserviceApplication.class, args);
    }

}
