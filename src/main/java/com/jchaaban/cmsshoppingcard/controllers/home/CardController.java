package com.jchaaban.cmsshoppingcard.controllers.home;

import com.jchaaban.cmsshoppingcard.models.CardItem;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/card")
@SuppressWarnings("unchecked")
public class CardController {

    @Autowired
    private ProductService productService;

    @GetMapping("/add/{id}")
    public String add(@PathVariable Integer id, HttpSession session, Model model){
        Product product = productService.findById(id);

        if (session.getAttribute("card") == null){
            HashMap<Integer, CardItem> card = new HashMap<>();
            card.put(id,new CardItem(product.getId(),product.getName(),product.getPrice(), 1,product.getImagePath()));
            session.setAttribute("card", card);
        } else {
            HashMap<Integer,CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
            if (card.containsKey(id)) {
                card.get(id).incrementQuantity();
            }
            else {
                card.put(id,new CardItem(product.getId(),product.getName(),product.getPrice(), 1,product.getImagePath()));
                session.setAttribute("card", card);
            }
        }

        HashMap<Integer,CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");

        int cardSize = 0;
        double cardTotal = 0;

        for (CardItem cardItem : card.values()) {
            cardSize+= cardItem.getQuantity();
            cardTotal+= cardItem.getQuantity() * Double.parseDouble(cardItem.getPrice());
        }

        model.addAttribute("cardSize", cardSize);
        model.addAttribute("cardTotal", cardTotal);


        return "card_view";
    }
}
