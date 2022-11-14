package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.OrderRepository;
import com.jchaaban.cmsshoppingcard.models.data.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void save(Order order){
        orderRepository.save(order);
    }

    public void add(){

    }
}
