package com.jchaaban.cmsshoppingcard.controllers.home;

import com.jchaaban.cmsshoppingcard.models.data.CardItem;
import com.jchaaban.cmsshoppingcard.models.data.Order;
import com.jchaaban.cmsshoppingcard.models.data.OrderItem;
import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.services.OrderService;
import com.jchaaban.cmsshoppingcard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/order")
@SuppressWarnings("unchecked")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    @GetMapping("/add")
    public String add(Order order, HttpSession session, Principal principal){
        System.out.println("we are hereeeeeeeeeeeeeere");

        User user = userService.getUserHavingUsername(principal.getName());
        order.setUser(user);
        System.out.println(principal.getName());

        HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");
        int totalQuantity = 0;
        double totalPrice = 0;
        for (CardItem cardItem : card.values()) {
            totalQuantity+= cardItem.getQuantity();
            totalPrice += Double.parseDouble(cardItem.getPrice()) * cardItem.getQuantity();
            order.add(new OrderItem(cardItem.getId(),cardItem.getPrice(),cardItem.getQuantity(), cardItem.getImagePath()));
        }

        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        order.setOrderTrackingNumber(UUID.randomUUID().toString());
        order.setAddress(user.getAddress());
        orderService.save(order);

        session.removeAttribute("card");


        return "redirect:/category/all";
    }


}
