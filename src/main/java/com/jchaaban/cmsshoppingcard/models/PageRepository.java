package com.jchaaban.cmsshoppingcard.models;

import com.jchaaban.cmsshoppingcard.models.data.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRepository extends JpaRepository<Page,Integer> {

//    @Override
//    List<Page> findAll(); // if i want to use crudRepository and if want this method to return a List

    Page findBySlug(String slug);

    Page findBySlugAndIdNot(String slug,Integer id);

    List<Page> findAllByOrderBySortingAsc();
}
