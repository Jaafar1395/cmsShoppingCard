package com.jchaaban.cmsshoppingcard.controllers.home;

import com.jchaaban.cmsshoppingcard.models.data.CardItem;
import com.jchaaban.cmsshoppingcard.models.data.Order;
import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.services.OrderService;
import com.jchaaban.cmsshoppingcard.services.UserService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@Controller
@RequestMapping("/order")
@SuppressWarnings("unchecked")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    @GetMapping("/add")
    public String add(Order order, HttpSession session, Principal principal)
            throws MessagingException, IOException, DocumentException {
        User user = userService.getUserHavingUsername(principal.getName());

        HashMap<Integer, CardItem> card = (HashMap<Integer, CardItem>) session.getAttribute("card");

        orderService.save(order,user,card);

        session.removeAttribute("card");

        orderService.sendOrderEmailToUser(order,user);

        return "redirect:/category/all";
    }


}
