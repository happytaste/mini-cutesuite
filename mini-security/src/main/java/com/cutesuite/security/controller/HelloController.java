package com.cutesuite.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Kevin zhang
 * @Date: 2020/5/6 13:47
 * @Version: 1.0
 * <p>Copyright: Copyright (c) </p>
 */
@RestController(value = "/")
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "hello at " + System.currentTimeMillis();
    }
}
