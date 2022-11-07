package com.jchaaban.cmsshoppingcard.controllers;

import com.jchaaban.cmsshoppingcard.models.CardItem;
import com.jchaaban.cmsshoppingcard.models.PageRepository;
import com.jchaaban.cmsshoppingcard.models.data.Category;
import com.jchaaban.cmsshoppingcard.models.data.Page;
import com.jchaaban.cmsshoppingcard.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class CommonController {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public void sharedData(HttpSession session, Model model) {
        List<Category> categories = categoryService.findAllByOrderBySortingAsc();
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();

        boolean cardActive = false;

        if (session.getAttribute("card") != null){
            HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
            int cardSize = 0;
            double cardTotal = 0;
            for (CardItem cardItem : card.values()) {
                cardSize+= cardItem.getQuantity();
                cardTotal+= cardItem.getQuantity() * Double.parseDouble(cardItem.getPrice());
            }

            model.addAttribute("cardSize", cardSize);
            model.addAttribute("cardTotal", cardTotal);

            cardActive = true;
        }

        model.addAttribute("cardActive", cardActive);
        model.addAttribute("commonPages", pages);
        model.addAttribute("commonCategories",categories);
    }
}
