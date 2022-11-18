package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.config.CmsShoppingCardProps;
import com.jchaaban.cmsshoppingcard.models.CategoryRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.utilities.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CmsShoppingCardProps properties;

    public List<Category> findAll(){
        return repository.findAll();
    }

    public List<Category> findAllByOrderBySortingAsc(){
        return repository.findAllByOrderBySortingAsc();
    }

    public Page<Category> findAllByOrderBySortingAscPage(Pageable pageable){
        return repository.findAllByOrderBySortingAsc(pageable);
    }

    public Category findById(Integer id){
        return repository.findById(id).get();
    }

    public Category findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    public void delete(Integer id) throws IOException {
        Category category = repository.findById(id).get();
        for (Product product : category.getProducts())
            FileUploadUtil.deleteFile(properties.getImgUploadDir(), product.getImage());
        repository.deleteById(id);
    }

    public boolean categoryExist(Category category, String categoryName, boolean isEditMode){
        Category existingCategory = repository.findByName(categoryName);
        if (existingCategory != null){
            if (isEditMode)
                return category.getId() != existingCategory.getId();
            return true;
        }
        return false;
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

    public void handelRedirectMessagesOnSuccess(RedirectAttributes redirectAttributes, String successMessage){
        redirectAttributes.addFlashAttribute("message", successMessage);
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
    }

    public void handelRedirectMessagesOnFailure(Category category, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "The Category you chose already exist");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        redirectAttributes.addFlashAttribute("categoryInfo", category);
    }

    public void reorder(int[] ids) {
        int count = 1;
        Category category;

        for(int pageId : ids){
            category = findById(pageId);
            category.setSorting(count++);
            save(category);
        }
    }

    private void saveFormCategory(Category category, String categoryName){
        String slug = setSlugUsingCategoryName(categoryName,category);
        category.setSlug(slug);
        repository.save(category);
    }

    private void save(Category category) {
        repository.save(category);
    }

    private String setSlugUsingCategoryName(String slug, Category category) {
        if (slug.trim().length() == 0)
            return category.getName().toLowerCase().replace(" ", "-");
        return slug.toLowerCase().replace(" ", "-");
    }

    public Long count() {
        return repository.count();
    }
}
