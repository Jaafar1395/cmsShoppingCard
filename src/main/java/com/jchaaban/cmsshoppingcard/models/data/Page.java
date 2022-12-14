package com.jchaaban.cmsshoppingcard.models.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "pages")
@Data
public class Page extends IDBasedEntity {

    @NotBlank(message = "This filed cannot be empty")
    @Size(min = 2, message = "Title must be at least 2 characters long")
    private String title;

    private String slug;

    @NotBlank(message = "This filed cannot be empty")
    @Size(min = 2, message = "Content must be at least 2 characters long")
    @Column(columnDefinition="LONGTEXT")
    private String content;

    private int sorting;
}
