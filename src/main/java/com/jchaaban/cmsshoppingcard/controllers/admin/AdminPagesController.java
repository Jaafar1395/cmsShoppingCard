package com.jchaaban.cmsshoppingcard.controllers.admin;

import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping
    public String index(Model model){
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();
        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Page page){
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        if (bindingResult.hasErrors())
            return "admin/pages/add";

        String slug = setSlugName(page.getSlug(), page);
        Page existingSlug = pageRepository.findBySlug(slug);

        if (existingSlug != null){
            handelRedirectMessagesOnFailure(page,redirectAttributes);
            return "redirect:/admin/pages/add";
        } else {
            handelRedirectMessagesOnSuccess(redirectAttributes,"Page was added successfully");
            page.setSlug(slug);
            page.setSorting(100);
            pageRepository.save(page);
        }

        return "redirect:/admin/pages";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        Page page = pageRepository.findById(id).get();
        model.addAttribute("page",page);
        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Page currentPage = pageRepository.findById(page.getId()).get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", currentPage.getTitle());
            return "admin/pages/edit";
        }

        String slug = setSlugName(page.getSlug(), page);
        Page existingSlug = pageRepository.findBySlugAndIdNot(slug,page.getId());

        if (existingSlug != null && existingSlug.getId() != page.getId()){
            handelRedirectMessagesOnFailure(page,redirectAttributes);
            return "redirect:/admin/pages/edit/" + page.getId();
        } else {
            handelRedirectMessagesOnSuccess(redirectAttributes,"Page was edited successfully");
            page.setSlug(slug);
            pageRepository.save(page);
        }

        return "redirect:/admin/pages";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes){
        pageRepository.deleteById(id);
        handelRedirectMessagesOnSuccess(redirectAttributes,"Page was deleted successfully");
        return "redirect:/admin/pages";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int [] ids){
        int count = 1;
        Page page;

        for(int pageId : ids){
            page = pageRepository.findById(pageId).get();
            page.setSorting(count++);
            pageRepository.save(page);
        }

        return "OK";
    }

    private String setSlugName(String slug, Page page) {
        if (slug.trim().length() == 0)
            return page.getTitle().toLowerCase().replace(" ", "-");
        return slug.toLowerCase().replace(" ", "-");
    }

    private void handelRedirectMessagesOnSuccess(RedirectAttributes redirectAttributes, String successMessage){
        redirectAttributes.addFlashAttribute("message", successMessage);
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
    }

    private void handelRedirectMessagesOnFailure(Page page, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("message", "The Slug you chose already exist");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        // for edit form id case the user entered slug that already exists
        redirectAttributes.addFlashAttribute("page", page);
    }

}
