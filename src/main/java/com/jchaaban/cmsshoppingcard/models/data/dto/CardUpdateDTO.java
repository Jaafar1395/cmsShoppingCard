package com.jchaaban.cmsshoppingcard.models.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardUpdateDTO {

    private double cardTotal;
    private double productTotalPrice;
    private int productQuantity;

    public CardUpdateDTO(double cardTotal,double productTotalPrice,int productQuantity){
        this.cardTotal = cardTotal;
        this.productTotalPrice = productTotalPrice;
        this.productQuantity = productQuantity;
    }
}
