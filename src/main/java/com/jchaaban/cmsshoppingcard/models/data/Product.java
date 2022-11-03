package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    private Long id;

    @NotBlank(message = "This filed cannot be empty")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    private String slug;

    @NotBlank(message = "This filed cannot be empty")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    private String image;

    @Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?", message = "Expected format: 5, 5,99, 15, 15,99")
    private String price;

    @Pattern(regexp = "^[1-9][0-9]*", message = "Please choose a category")
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
