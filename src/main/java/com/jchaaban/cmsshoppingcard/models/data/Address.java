package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Table(name = "address")
@Entity
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "This filed cannot be empty")
    @Size(min = 10, message = "Street must be at least 15 characters long")
    @Column(name = "street")
    private String street;

    @NotBlank(message = "This filed cannot be empty")
    @Size(min = 3, message = "City must be at least 3 characters long")
    @Column(name = "city")
    private String city;

    @Pattern(regexp = "\\b\\d{5}\\b", message = "Postal code must be 5 digits")
    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne
    @PrimaryKeyJoinColumn // den id als joinColumn nutzen
    private User user;

}
