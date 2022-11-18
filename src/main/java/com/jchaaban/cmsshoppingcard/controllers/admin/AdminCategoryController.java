package com.jchaaban.cmsshoppingcard.controllers.admin;

import com.jchaaban.cmsshoppingcard.config.CmsShoppingCardProps;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CmsShoppingCardProps properties;

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer pageNum){
        int page = pageNum == null ? 0 : pageNum;
        int perPage = properties.getStandardPageSize();
        Pageable pageable = PageRequest.of(page,perPage);
        Long count = categoryService.count();
        double pageCount = Math.ceil((double) count / (double) perPage);
        Page<Category> categories = categoryService.findAllByOrderBySortingAscPage(pageable);
        model.addAttribute("categories", categories);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

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
        categoryService.reorder(ids);
        return "OK";
    }
}
