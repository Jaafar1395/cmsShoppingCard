package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.utilities.FileUploadUtil;
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
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryRepository.findAllByOrderBySortingAsc();
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

        String slug = setSlugUsingCategoryName(category.getName(), category);
        Category categoryHavingSameSlug = categoryRepository.findByName(slug);

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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        Category category = categoryRepository.findById(id).get();
        model.addAttribute("category",category);
        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Category currentCategory = categoryRepository.findById(category.getId()).get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryTitle", currentCategory.getName());
            return "admin/categories/edit";
        }

        String categoryName = category.getName();
        Category existingCategory = categoryRepository.findByName(categoryName);
        if (existingCategory != null && existingCategory.getId() != category.getId()){
            handelRedirectMessagesOnFailure(category,redirectAttributes);
            return "redirect:/admin/categories/edit/" + category.getId();
        } else {
            handelRedirectMessagesOnSuccess(redirectAttributes,"Category was edited successfully");
            String slug = setSlugUsingCategoryName(categoryName,category);
            category.setSlug(slug);
            categoryRepository.save(category);
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) throws IOException {
        Category category = categoryRepository.findById(id).get();
        String categoryName = category.getName();
        for (Product product : category.getProducts())
            FileUploadUtil.deleteFile("media/" + categoryName, product.getImage());
        categoryRepository.deleteById(id);
        handelRedirectMessagesOnSuccess(redirectAttributes,"Category was deleted successfully");
        return "redirect:/admin/categories";
    }

    @PostMapping("/reorder")
    public @ResponseBody
    String reorder(@RequestParam("id[]") int [] ids){
        int count = 1;
        Category page;

        for(int pageId : ids){
            page = categoryRepository.findById(pageId).get();
            page.setSorting(count++);
            categoryRepository.save(page);
        }

        return "OK";
    }


    private String setSlugUsingCategoryName(String slug, Category category) {
        if (slug.trim().length() == 0)
            return category.getName().toLowerCase().replace(" ", "-");
        return slug.toLowerCase().replace(" ", "-");
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
