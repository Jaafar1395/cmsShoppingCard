package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.ProductRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.services.ProductService;
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
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;

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

        if (productService.noFileUploaded(file))
            productService.handelNoFileUploaded(product,attributes);
        else if (productService.invalidFileFormat(filename))
            productService.handelInvalidFile(product,attributes);
        else if(productService.productExist(product,slug,false))
            productService.handelProductExists(product,attributes);
        else {
            productService.saveNewProduct(product,file,filename,slug,attributes);
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

        Product existingProduct = productRepository.findById(product.getId()).get();

        if (bindingResult.hasErrors()){
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("productOldImage", existingProduct.getImagePath());
            return "/admin/products/edit";
        }

        boolean newFileUploaded = !productService.noFileUploaded(file);

        String filename;
        if (newFileUploaded)
            filename = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll(" ", "");
        else
            filename = existingProduct.getImage();

        String slug = product.getName().toLowerCase().replace(" ", "-");

        if (newFileUploaded && productService.invalidFileFormat(filename))
            productService.handelInvalidFile(product,attributes);
        else if(productService.productExist(product,slug,true))
            productService.handelProductExists(product,attributes);
        else {
            productService.saveEditedProduct(product,existingProduct,file,filename,slug,newFileUploaded,attributes);
            return "redirect:/admin/products";
        }

        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes attributes) throws IOException {
        productService.deleteProduct(id);
        productService.handelSuccessOperation(null, "Product was successfully deleted", attributes);
        return "redirect:/admin/products";
    }

}
