package com.jchaaban.cmsshoppingcard.models;

import com.jchaaban.cmsshoppingcard.models.data.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    Product findBySlug(String slug);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByCategoryId(Integer categoryId, Pageable pageable);

    long countByCategoryId(Integer categoryId);
}
