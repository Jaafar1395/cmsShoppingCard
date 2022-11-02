package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories/index";
    }
}
