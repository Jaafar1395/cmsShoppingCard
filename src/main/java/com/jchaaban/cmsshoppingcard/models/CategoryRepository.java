package com.jchaaban.cmsshoppingcard.models;

import com.jchaaban.cmsshoppingcard.models.data.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Category findByName(String name);

    List<Category> findAllByOrderBySortingAsc();
    Page<Category> findAllByOrderBySortingAsc(Pageable pageable);

    Category findBySlug(String slug);
}


