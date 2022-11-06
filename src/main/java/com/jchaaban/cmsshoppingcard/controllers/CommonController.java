package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import com.jchaaban.cmsshoppingcard.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class CommonController {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public void home(Model model) {
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();
        model.addAttribute("commonPages", pages);
        List<Category> categories = categoryService.findAllByOrderBySortingAsc();
        model.addAttribute("commonCategories",categories);
    }
}
