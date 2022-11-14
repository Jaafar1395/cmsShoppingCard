package com.jchaaban.cmsshoppingcard.models;

import com.jchaaban.cmsshoppingcard.models.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
