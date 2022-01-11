package com.yk.system;

import com.yk.common.swagger.annotation.EnableCustomSwagger2;
import com.yk.security.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统模块
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableFeignClients
@SpringBootApplication
public class YkSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(YkSystemApplication.class, args);
        System.out.println("系统模块启动成功");
    }
}
