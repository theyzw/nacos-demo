package com.yk.calc;

import com.yk.common.swagger.annotation.EnableCustomSwagger2;
import com.yk.security.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.yk")
@ComponentScan("com.yk")
@EnableCustomSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class YkCalcApplication {

    public static void main(String[] args) {
        SpringApplication.run(YkCalcApplication.class, args);
        System.out.println("计算模块启动成功");
    }
}
