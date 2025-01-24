package com.wildlifebackend.wildlife.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/test")
public class Test {
    @GetMapping(value = "/ok")
    public String ok() {
        return "ok";
    }

}
