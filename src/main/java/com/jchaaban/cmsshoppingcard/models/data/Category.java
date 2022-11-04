package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Table(name = "categories")
@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "This filed cannot be empty")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    private String slug;

    private int sorting;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Product> products;

}
