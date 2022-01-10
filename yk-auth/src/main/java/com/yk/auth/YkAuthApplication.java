package com.yk.auth;

//import com.ruoyi.common.security.annotation.EnableRyFeignClients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 认证授权中心
 */
//@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class YkAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(YkAuthApplication.class, args);
        System.out.println("认证授权中心启动成功");
    }
}
