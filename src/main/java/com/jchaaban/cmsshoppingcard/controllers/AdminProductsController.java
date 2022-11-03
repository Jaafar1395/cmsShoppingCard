package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.ProductRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.utilities.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model){
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "/admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model){
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "/admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product,
                    BindingResult bindingResult,
                      @RequestParam("photo") MultipartFile file,
                    RedirectAttributes attributes, Model model) throws IOException {

        if (bindingResult.hasErrors()){
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            return "/admin/products/add";
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll(" ", "");
        String slug = product.getName().toLowerCase().replace(" ", "-");
        Product existingProduct = productRepository.findBySlug(slug);

        if (file.getSize() == 0)
            handelRedirectMessages(
                    product,
                    attributes,
                    "Please select an image for the product",
                    "alert-danger");
        else if (!(filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg"))) handelRedirectMessages(
                product,
                attributes,
                "Image must have on of the following extensions: jpg, jpeg, png",
                "alert-danger");
        else if(existingProduct != null) handelRedirectMessages(
                product,
                attributes,
                "The product you tried to add already exists",
                "alert-danger");
        else {
            product.setSlug(slug);
            product.setImage(filename);
            String uploadDirectory = "src/main/resources/media";
            FileUploadUtil.saveFile(uploadDirectory, filename, file);
            productRepository.save(product);
            handelRedirectMessages(null,attributes, "Product was successfully added", "alert-success");
            return "redirect:/admin/products";
        }
        return "redirect:/admin/products/add";
    }

    private void handelRedirectMessages(Product product,RedirectAttributes redirectAttributes,String message, String alertClass){
        redirectAttributes.addFlashAttribute("product", product);
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("alertClass", alertClass);
    }



}
