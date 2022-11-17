package com.jchaaban.cmsshoppingcard.controllers.home;

import com.jchaaban.cmsshoppingcard.models.data.CardItem;
import com.jchaaban.cmsshoppingcard.models.data.dto.CardUpdateDTO;
import com.jchaaban.cmsshoppingcard.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/card/json")
@SuppressWarnings("unchecked")
public class CardRestController {

    @Autowired
    private CardService cardService;

    @GetMapping("/add/{id}")
    public CardUpdateDTO add(@PathVariable Integer id, HttpSession session){
        HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
        cardService.incrementProductAmount(card,id);
        double cardTotal = cardService.getCardTotal(card);
        double productTotalPrice = cardService.getProductTotalPrice(card,id);
        int productQuantity = cardService.getProductQuantity(card,id);
        return new CardUpdateDTO(cardTotal,productTotalPrice,productQuantity);
    }

    @GetMapping("/subtract/{id}")
    public CardUpdateDTO subtract(@PathVariable Integer id, HttpSession session){
        HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
        cardService.subtract(id,session);
        double cardTotal = cardService.getCardTotal(card);
        int productQuantity = cardService.getProductQuantity(card,id);
        double productTotalPrice = 0;
        if (productQuantity != 0)
            productTotalPrice = cardService.getProductTotalPrice(card,id);
        return new CardUpdateDTO(cardTotal,productTotalPrice,productQuantity);
    }

    @GetMapping("/remove/{id}")
    public CardUpdateDTO remove(@PathVariable Integer id, HttpSession session){
        HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
        cardService.remove(id,session);
        double cardTotal = cardService.getCardTotal(card);
        return new CardUpdateDTO(cardTotal,0,0);
    }

}
