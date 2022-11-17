package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.OrderRepository;
import com.jchaaban.cmsshoppingcard.models.data.*;
import com.jchaaban.cmsshoppingcard.utilities.OrderPdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    public void save(Order order, User user, HashMap<Integer, CardItem> card){

        int totalQuantity = 0;
        double totalPrice = 0;
        for (CardItem cardItem : card.values()) {
            totalQuantity+= cardItem.getQuantity();
            totalPrice += Double.parseDouble(cardItem.getPrice()) * cardItem.getQuantity();
            order.add(new OrderItem(
                    cardItem.getId(),cardItem.getName(),
                    cardItem.getPrice(),cardItem.getQuantity(),
                    cardItem.getImagePath()));
        }

        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        order.setOrderTrackingNumber(UUID.randomUUID().toString());
        order.setUser(user);
        order.setAddress(user.getAddress());

        orderRepository.save(order);
    }

    public void sendOrderEmailToUser(Order order,User user) throws IOException, MessagingException {
        OrderPdfExporter exporter = new OrderPdfExporter();
        File file = exporter.exportOrderToPdf(order,order.getOrderTrackingNumber());
        emailService.sendEmailWithFileAttachment(user.getUsername(), user.getEmail(),file);
        Files.delete(file.toPath());
    }

    public Page<Order> findAllByOrderByDateCreatedAsc(Pageable pageable){
        return orderRepository.findAllByOrderByDateCreatedAsc(pageable);
    }

    public Order getOrderById(Integer id){
        return orderRepository.findById(id).get();
    }

    public long count(){
        return orderRepository.count();
    }
}
