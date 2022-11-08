package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.CardItem;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@SuppressWarnings("unchecked")
public class CardService {

    @Autowired
    private ProductService productService;

    public void updateCardStatus(HttpSession session, Model model){

        int cardSize = 0, cardTotal = 0;

        HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");

        for (CardItem cardItem : card.values()) {
            cardSize+= cardItem.getQuantity();
            cardTotal+= cardItem.getQuantity() * Double.parseDouble(cardItem.getPrice());
        }

        model.addAttribute("cardSize", cardSize);
        model.addAttribute("cardTotal", cardTotal);

    }

    public void add(Integer id,HttpSession session, Model model){
        Product product = productService.findById(id);

        if (session.getAttribute("card") == null){
            HashMap<Integer, CardItem> card = new HashMap<>();
            card.put(id,new CardItem(product.getId(),product.getName(),product.getPrice(), 1,product.getImagePath()));
            session.setAttribute("card", card);
        } else {
            HashMap<Integer,CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
            if (card.containsKey(id)) {
                card.get(id).incrementQuantity();
            } else {
                card.put(id,new CardItem(product.getId(),product.getName(),product.getPrice(), 1,product.getImagePath()));
                session.setAttribute("card", card);
            }
        }

        updateCardStatus(session,model);
    }

}
