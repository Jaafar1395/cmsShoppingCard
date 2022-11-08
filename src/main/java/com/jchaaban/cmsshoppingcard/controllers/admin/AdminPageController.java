package com.jchaaban.cmsshoppingcard.controllers.admin;

import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import com.jchaaban.cmsshoppingcard.services.PageService;
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
public class AdminPageController {

    @Autowired
    private PageService pageService;

    @GetMapping
    public String index(Model model){
        List<Page> pages = pageService.findAllByOrderBySortingAsc();
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

        String slug = pageService.setSlug(page.getSlug(), page);

        if (pageService.pageExist(page,slug,false)){
            pageService.handelRedirectMessagesOnFailure(page,redirectAttributes);
            return "redirect:/admin/pages/add";
        } else {
            pageService.saveNewPage(page,slug, redirectAttributes);
        }

        return "redirect:/admin/pages";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        Page page = pageService.findById(id);
        model.addAttribute("page",page);
        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Page currentPage = pageService.findById(page.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", currentPage.getTitle());
            return "admin/pages/edit";
        }

        String slug = pageService.setSlug(page.getSlug(), page);

        if (pageService.pageExist(page,slug,true)){
            pageService.handelRedirectMessagesOnFailure(page,redirectAttributes);
            return "redirect:/admin/pages/edit/" + page.getId();
        } else {
            pageService.saveEditedPage(page,slug,redirectAttributes);
        }

        return "redirect:/admin/pages";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes){
        pageService.deleteById(id,redirectAttributes);
        return "redirect:/admin/pages";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int [] ids){
        pageService.reorder(ids);
        return "OK";
    }

}
