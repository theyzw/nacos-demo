package com.yk.calc.controller;

import com.yk.calc.service.CalcService;
import com.yk.common.core.domain.ApiResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
@Api(tags = "test")
@RequestMapping("/test")
public class CalcController {
    
    @Autowired
    private CalcService calcService;

    @GetMapping("/test")
    public ApiResult test() {
        log.info("calc test");
        calcService.test(1, 20);

        return ApiResult.ok();
    }

}
