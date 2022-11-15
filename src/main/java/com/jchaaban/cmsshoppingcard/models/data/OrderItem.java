package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem extends IDBasedEntity {

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "unit_price")
    private String unitPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem() {}

    public OrderItem(Integer productId, String price, int quantity, String imagePath) {
        this.productId = productId;
        this.unitPrice = price;
        this.quantity = quantity;
        this.imageUrl = imagePath;
    }
}
