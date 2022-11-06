package com.jchaaban.cmsshoppingcard.models;

import lombok.Data;

@Data
public class Card {

    private Integer id;
    private String name;
    private String price;
    private String quantity;
    private String image;

    public Card(Integer id, String name, String price, String quantity, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }
}
