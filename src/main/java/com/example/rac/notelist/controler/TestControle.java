package com.example.rac.notelist.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class TestControle {

    @GetMapping("/test")
    public String test(){

        return "test";
    }
}
