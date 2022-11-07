package com.jchaaban.cmsshoppingcard.controllers.home;

import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import com.jchaaban.cmsshoppingcard.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePageController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping
    public String home(Model model){
        Page page = pageRepository.findBySlug("home");
        model.addAttribute("page",page);
        return "page";
    }

    @GetMapping("/{slug}")
    public String page(@PathVariable("slug") String slug, Model model){
        Page page = pageRepository.findBySlug(slug);
        model.addAttribute("page",page);

        if (page == null)
            return "redirect:/";

        return "page";
    }
}
