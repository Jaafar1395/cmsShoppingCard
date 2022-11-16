package com.jchaaban.cmsshoppingcard.controllers.admin;

import com.jchaaban.cmsshoppingcard.models.data.Order;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String orders(Model model, @RequestParam(value = "page", required = false) Integer pageNum){
        int page = pageNum == null ? 0 : pageNum;
        int perPage = 10;
        Pageable pageable = PageRequest.of(page,perPage);
        Long count = orderService.count();
        double pageCount = Math.ceil((double) count / (double) perPage);
        Page<Order> orders = orderService.findAllByOrderByDateCreatedAsc(pageable);

        model.addAttribute("orders", orders);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/orders/index";
    }




}