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
import org.springframework.web.bind.annotation.*;
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
            String uploadDirectory = "media/" + product.getCategory().getName();
            FileUploadUtil.saveFile(uploadDirectory, filename, file);
            productRepository.save(product);
            handelRedirectMessages(null,attributes, "Product was successfully added", "alert-success");
            return "redirect:/admin/products";
        }
        return "redirect:/admin/products/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        Product product = productRepository.findById(id).get();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product",product);
        return "admin/products/edit";
    }

    // todo check why images are not showing when changing the category

    @PostMapping("/edit")
    public String edit(@Valid Product product,
                      BindingResult bindingResult,
                      @RequestParam("photo") MultipartFile file,
                      RedirectAttributes attributes, Model model) throws IOException {

        Product currentProduct = productRepository.findById(product.getId()).get();

        if (bindingResult.hasErrors()){
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("productOldImage", currentProduct.getImage());
            return "/admin/products/edit";
        }

        boolean newFileUploaded = file.getSize() > 0;
        String filename;
        if (newFileUploaded)
            filename = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll(" ", "");
        else
            filename = currentProduct.getImage();

        String slug = product.getName().toLowerCase().replace(" ", "-");
        Product existingProduct = productRepository.findBySlug(slug);

        if (newFileUploaded && !(filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg"))) handelRedirectMessages(
                product,
                attributes,
                "Image must have on of the following extensions: jpg, jpeg, png",
                "alert-danger");
        else if(existingProduct != null && existingProduct.getId() != product.getId()) handelRedirectMessages(
                product,
                attributes,
                "The product you tried to add already exists",
                "alert-danger");
        else {
            product.setSlug(slug);
            product.setImage(filename);
            String uploadDirectory = "media/" + product.getCategory().getName();
            if (newFileUploaded || currentProduct.getCategory().getName() != product.getCategory().getName()) {
                FileUploadUtil.saveFile(uploadDirectory, filename, file);
                FileUploadUtil.deleteFile("media/" + currentProduct.getCategory().getName(), currentProduct.getImage());
            }
            handelRedirectMessages(null,attributes, "Product was successfully added", "alert-success");
            productRepository.save(product);

            return "redirect:/admin/products";
        }
        return "redirect:/admin/products/edit/" + product.getId();
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes attributes) throws IOException {
        Product product = productRepository.findById(id).get();
        productRepository.deleteById(product.getId());
        FileUploadUtil.deleteFile("media/" + product.getCategory().getName(), product.getImage());
        handelRedirectMessages(null,attributes, "Product was successfully deleted", "alert-success");
        return "redirect:/admin/products";
    }


    private void handelRedirectMessages(Product product,RedirectAttributes redirectAttributes,String message, String alertClass){
        redirectAttributes.addFlashAttribute("product", product);
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("alertClass", alertClass);
    }

}
