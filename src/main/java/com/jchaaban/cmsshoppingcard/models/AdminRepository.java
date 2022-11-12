package com.jchaaban.cmsshoppingcard.models;

import com.jchaaban.cmsshoppingcard.models.data.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {

    Admin findByUsername(String username);
}
