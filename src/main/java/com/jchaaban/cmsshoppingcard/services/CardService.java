package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.data.CardItem;
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

        double cardSize = 0, cardTotal = 0;

        HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");

        for (CardItem cardItem : card.values()) {
            cardSize+= cardItem.getQuantity();
            cardTotal+= cardItem.getQuantity() * Double.parseDouble(cardItem.getPrice());
        }

        model.addAttribute("cardSize", cardSize);
        model.addAttribute("cardTotal", cardTotal);

    }

    public double getCardTotal(HashMap<Integer, CardItem> card){
        double cardTotal = 0;

        for (CardItem cardItem : card.values())
            cardTotal+= cardItem.getQuantity() * Double.parseDouble(cardItem.getPrice());


        return cardTotal;
    }

    public void incrementProductAmount(HashMap<Integer,CardItem> card,Integer id){
        card.get(id).incrementQuantity();
    }

    public double getProductTotalPrice(HashMap<Integer,CardItem> card,Integer id){
        CardItem cardItem = card.get(id);
        return cardItem.getQuantity() * Double.parseDouble(cardItem.getPrice());
    }

    public int getProductQuantity(HashMap<Integer,CardItem> card,Integer id){
        CardItem cardItem = card.get(id);
        if (cardItem != null)
            return cardItem.getQuantity();
        return 0;
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
                incrementProductAmount(card,id);
            } else {
                card.put(id,new CardItem(product.getId(),product.getName(),product.getPrice(), 1,product.getImagePath()));
                session.setAttribute("card", card);
            }
        }

        updateCardStatus(session,model);
    }


    public void subtract(Integer id, HttpSession session){
        Product product = productService.findById(id);
        HashMap<Integer,CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
        int quantity = card.get(id).getQuantity();
        if (quantity == 1){
            card.remove(id);
            if (card.size() == 0)
                session.removeAttribute("cart");
        } else {
            card.put(id,new CardItem(product.getId(),product.getName(),product.getPrice(), --quantity,product.getImagePath()));
        }
    }

    public void remove(Integer id, HttpSession session){
        HashMap<Integer,CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
        card.remove(id);

        if (card.size() == 0)
            session.removeAttribute("card");
    }
}
