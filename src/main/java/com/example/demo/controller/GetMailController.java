package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class GetMailController {
    
    @GetMapping("/mail")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("<h1>Hello %s!</h1>", name);
    }
}
