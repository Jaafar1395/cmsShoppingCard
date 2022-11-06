package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryService.findAllByOrderBySortingAsc();
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

        String categoryName = category.getName();

        if (categoryService.categoryExist(category,category.getName(),false)){
            categoryService.handelRedirectMessagesOnFailure(category,redirectAttributes);
            return "redirect:/admin/categories/add";
        } else {
            categoryService.saveNewCategory(category,categoryName,redirectAttributes);
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        Category category = categoryService.findById(id);
        model.addAttribute("category",category);
        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Category currentCategory = categoryService.findById(category.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryTitle", currentCategory.getName());
            return "admin/categories/edit";
        }

        String categoryName = category.getName();

        if (categoryService.categoryExist(category,categoryName,true)){
            categoryService.handelRedirectMessagesOnFailure(category,redirectAttributes);
            return "redirect:/admin/categories/edit/" + category.getId();
        } else {
            categoryService.saveEditedCategory(category,categoryName,redirectAttributes);
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) throws IOException {
        categoryService.delete(id);
        categoryService.handelRedirectMessagesOnSuccess(redirectAttributes,"Category was deleted successfully");
        return "redirect:/admin/categories";
    }

    @PostMapping("/reorder")
    public @ResponseBody
    String reorder(@RequestParam("id[]") int [] ids){
        int count = 1;
        Category category;

        for(int pageId : ids){
            category = categoryService.findById(pageId);
            category.setSorting(count++);
            categoryService.save(category);
        }

        return "OK";
    }
}
