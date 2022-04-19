package com.zhouyue.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/employee/basic/test")
    public String test2(){
        return "/employee/basic/test";
    }

    @GetMapping("/employee/advanced/test")
    public String test3(){
        return "/employee/advanced/test";
    }
}
