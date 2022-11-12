package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.AdminRepository;
import com.jchaaban.cmsshoppingcard.models.UserRepository;
import com.jchaaban.cmsshoppingcard.models.data.Admin;
import com.jchaaban.cmsshoppingcard.models.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        Admin admin = adminRepository.findByUsername(username);

        if (user != null)
            return user;

        if (admin != null)
            return admin;

        throw new UsernameNotFoundException("User having username: " + username + " was not found");

    }
}
