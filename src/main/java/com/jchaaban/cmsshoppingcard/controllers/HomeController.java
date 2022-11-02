package com.jchaaban.cmsshoppingcard.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // for component scanning
public class HomeController {

    @GetMapping("/test")
    public String home(){
        return "home";
    }
}
