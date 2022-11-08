package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
public class PageService {

    @Autowired
    private PageRepository repository;

    public Page findBySlug(String slug){
        return repository.findBySlug(slug);
    }

    public Page findBySlugAndIdNot(String slug,Integer id){
        return repository.findBySlugAndIdNot(slug,id);
    }

    public List<Page> findAllByOrderBySortingAsc(){
        return repository.findAllByOrderBySortingAsc();
    }

    public Page findById(Integer id) {
        return repository.findById(id).get();
    }

    public void deleteById(Integer id, RedirectAttributes redirectAttributes){
        repository.deleteById(id);
        handelRedirectMessagesOnSuccess(redirectAttributes,"Page was deleted successfully");
    }

    public void saveNewPage(Page page, String slug, RedirectAttributes redirectAttributes) {
        page.setSorting(100);
        saveFormPage(page,slug);
        handelRedirectMessagesOnSuccess(redirectAttributes,"Page was added successfully");
    }

    public void saveEditedPage(Page page, String slug, RedirectAttributes redirectAttributes) {
        saveFormPage(page,slug);
        handelRedirectMessagesOnSuccess(redirectAttributes,"Category was edited successfully");
    }

    public void reorder(int[] ids) {
        int count = 1;
        Page page;

        for(int pageId : ids){
            page = repository.findById(pageId).get();
            page.setSorting(count++);
            repository.save(page);
        }
    }

    private void saveFormPage(Page page, String slug){;
        page.setSlug(slug);
        save(page);
    }

    private void save(Page page) {
        repository.save(page);
    }

    public boolean pageExist(Page category, String pageSlug, boolean editMode){
        Page existingPage = repository.findBySlug(pageSlug);
        if (existingPage != null){
            if (editMode)
                return category.getId() != existingPage.getId();
            return true;
        }
        return false;
    }

    public String setSlug(String slug, Page page) {
        if (slug.trim().length() == 0)
            return page.getTitle().toLowerCase().replace(" ", "-");
        return slug.toLowerCase().replace(" ", "-");
    }

    private void handelRedirectMessagesOnSuccess(RedirectAttributes redirectAttributes, String successMessage){
        redirectAttributes.addFlashAttribute("message", successMessage);
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
    }

    public void handelRedirectMessagesOnFailure(Page page, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "The Slug you chose already exist");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        redirectAttributes.addFlashAttribute("page", page);
    }
}
