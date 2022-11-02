package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

    @GetMapping("/add")
    public String add(Category category){
        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors())
            return "admin/categories/add";

        String slug = setSlugName(category.getName(), category);
        Category categoryHavingSameSlug = categoryRepository.findBySlug(slug);

        if (categoryHavingSameSlug != null){
            handelRedirectMessagesOnFailure(category,redirectAttributes);
            return "redirect:/admin/categories/add";
        } else {
            handelRedirectMessagesOnSuccess(redirectAttributes,"Page was added successfully");
            category.setSlug(slug);
            category.setSorting(100);
            categoryRepository.save(category);
        }

        return "redirect:/admin/categories";
    }

    private String setSlugName(String slug, Category page) {
        if (slug.trim().length() == 0)
            slug = page.getSlug().toLowerCase().replace(" ", "-");
        else
            slug = slug.toLowerCase().replace(" ", "-");
        return slug;
    }

    private void handelRedirectMessagesOnSuccess(RedirectAttributes redirectAttributes, String successMessage){
        redirectAttributes.addFlashAttribute("message", successMessage);
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
    }

    private void handelRedirectMessagesOnFailure(Category category, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "The Category you chose already exist");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        redirectAttributes.addFlashAttribute("categoryInfo", category);
    }


}
