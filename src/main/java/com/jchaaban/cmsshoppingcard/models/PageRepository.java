package com.jchaaban.cmsshoppingcard.models;

import com.jchaaban.cmsshoppingcard.models.data.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PageRepository extends JpaRepository<Page,Long> {

//    @Override
//    List<Page> findAll(); // if i want to use crudRepository and if want this method to return a List

    Page findBySlug(String slug);

//    @Query("SELECT p FROM Page p WHERE p.id != :id and p.slug = :slug")
    Page findBySlugAndIdNot(String slug,Long id);
}
