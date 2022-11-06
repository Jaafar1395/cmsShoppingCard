package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class CommonController {

    @Autowired
    private PageRepository pageRepository;

    @ModelAttribute
    public void home(Model model) {
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();
        model.addAttribute("commonPages", pages);
    }
}
