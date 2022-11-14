package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "zipcode")
@Getter
@Setter
public class ZipCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "zipcode")
    private String zipcode;

}
