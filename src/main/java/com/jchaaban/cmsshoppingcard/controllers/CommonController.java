package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CardItem;
import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import com.jchaaban.cmsshoppingcard.services.CardService;
import com.jchaaban.cmsshoppingcard.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class CommonController {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CardService cardService;

    @ModelAttribute
    public void sharedData(HttpSession session, Model model) {
        List<Category> categories = categoryService.findAllByOrderBySortingAsc();
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();

        if (session.getAttribute("card") != null){
            cardService.updateCardStatus(session,model);
        }

        model.addAttribute("commonPages", pages);
        model.addAttribute("commonCategories",categories);
    }
}
