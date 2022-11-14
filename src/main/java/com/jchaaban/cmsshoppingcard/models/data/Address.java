package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "address")
@Entity
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn // den id als joinColumn nutzen
    private User user;

}
