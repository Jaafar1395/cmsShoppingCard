package com.jchaaban.cmsshoppingcard.controllers.home;

import com.jchaaban.cmsshoppingcard.models.data.CardItem;
import com.jchaaban.cmsshoppingcard.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/card")
@SuppressWarnings("unchecked")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/add/{id}")
    public String add(@PathVariable Integer id, HttpSession session, Model model,
                      @RequestParam(name = "cardPage", required = false) String cardPage){

        cardService.add(id,session,model);

        if (cardPage != null) return "redirect:/card/details";

        return "card_view";
    }

    @GetMapping("/clear")
    public String clear(HttpSession session, Model model,
                        @RequestParam(name = "cardPage", required = false) String cardPage){

        session.removeAttribute("card");

        if (cardPage != null) {
            model.addAttribute("cardSize", null);
            model.addAttribute("cardTotal", null);
            return "card_view";
        }
        return "redirect:/category/all";
    }


    @GetMapping("/details")
    public String cardDetails(HttpSession session, Model model){

        if (session.getAttribute("card") == null)
            return "redirect:/";

        HashMap<Integer,CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
        model.addAttribute("card", card);
        model.addAttribute("checkoutDetailsPage", true);
        return "card_details";
    }
}
