package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.utilities.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll(){
        return repository.findAll();
    }

    public List<Category> findAllByOrderBySortingAsc(){
        return repository.findAllByOrderBySortingAsc();
    }

    public Category findById(Integer id){
        return repository.findById(id).get();
    }

    public void delete(Integer id) throws IOException {
        Category category = repository.findById(id).get();
        String categoryName = category.getName();
        for (Product product : category.getProducts())
            FileUploadUtil.deleteFile("media/" + categoryName, product.getImage());
        repository.deleteById(id);
    }

    public boolean categoryExist(Category category, String categoryName, boolean editMode){
        Category existingCategory = repository.findByName(categoryName);
        if (existingCategory != null){
            if (editMode)
                return category.getId() != existingCategory.getId();
            return true;
        }
        return false;
    }

    public String setSlugUsingCategoryName(String slug, Category category) {
        if (slug.trim().length() == 0)
            return category.getName().toLowerCase().replace(" ", "-");
        return slug.toLowerCase().replace(" ", "-");
    }

    public void saveNewCategory(Category category, String categoryName, RedirectAttributes redirectAttributes) {
        category.setSorting(100);
        saveFormCategory(category,categoryName);
        handelRedirectMessagesOnSuccess(redirectAttributes,"Page was added successfully");
    }

    public void saveEditedCategory(Category category, String categoryName, RedirectAttributes redirectAttributes) {
        saveFormCategory(category,categoryName);
        handelRedirectMessagesOnSuccess(redirectAttributes,"Category was edited successfully");
    }

    private void saveFormCategory(Category category, String categoryName){
        String slug = setSlugUsingCategoryName(categoryName,category);
        category.setSlug(slug);
        repository.save(category);
    }

    public void save(Category category) {
        repository.save(category);
    }

    public void handelRedirectMessagesOnSuccess(RedirectAttributes redirectAttributes, String successMessage){
        redirectAttributes.addFlashAttribute("message", successMessage);
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
    }

    public void handelRedirectMessagesOnFailure(Category category, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "The Category you chose already exist");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        redirectAttributes.addFlashAttribute("categoryInfo", category);
    }

}
