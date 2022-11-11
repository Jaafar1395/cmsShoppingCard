package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Data;

@Data
public class CardItem {

    private Integer id;
    private String name;
    private String price;
    private int quantity;
    private String imagePath;

    public CardItem(Integer id, String name, String price, int quantity, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }



    public void incrementQuantity(){++quantity;}
}
