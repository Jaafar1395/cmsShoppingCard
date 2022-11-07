package com.jchaaban.cmsshoppingcard.models;

import lombok.Data;

@Data
public class CardItem {

    private Integer id;
    private String name;
    private String price;
    private int quantity;
    private String imagePath;

    public CardItem(Integer id, String name, String price, int quantity, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = image;
    }

    public void incrementQuantity(){++quantity;}
}
