package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.services.CategoryService;
import com.jchaaban.cmsshoppingcard.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/{slug}")
    public String category(@PathVariable String slug, Model model,
                           @RequestParam(value = "page", required = false) Integer pageNum){
        int page = pageNum == null ? 0 : pageNum;
        int perPage = 2;
        Pageable pageable = PageRequest.of(page,perPage);
        long count;
        Page<Product> products;

        if (slug.equals("all")){
            products = productService.findAll(pageable);
            count = productService.count();
        } else {
            Category category = categoryService.findBySlug(slug);
            if (category == null)
                return "redirect:/";
            int categoryId = category.getId();
            String categoryName = category.getName();
            products = productService.findAllByCategoryId(categoryId,pageable);
            count = productService.countByCategoryId(category.getId());
            model.addAttribute("categoryName", categoryName);
        }

        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("products", products);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("count", count);
        model.addAttribute("perPage", perPage);
        model.addAttribute("page", page);

        return "products";
    }


}