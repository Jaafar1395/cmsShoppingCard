package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePageController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping
    public String home(Model model){
        Page page = pageRepository.findBySlug("home");
        model.addAttribute("page",page);

        return "page";
    }
}
