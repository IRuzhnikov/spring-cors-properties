package com.github.iruzhnikov.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("/withCors")
    public String getWithCors() {
        return "gotit";
    }

    @GetMapping("/withoutCors")
    public String getWithoutCors() {
        return "gotit";
    }

    @PostMapping("/post")
    public String post() {
        return "gotit";
    }
}
