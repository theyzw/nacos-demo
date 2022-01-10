package com.yk.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class YkGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YkGatewayApplication.class, args);
        log.info("网关启动成功");
    }

}
