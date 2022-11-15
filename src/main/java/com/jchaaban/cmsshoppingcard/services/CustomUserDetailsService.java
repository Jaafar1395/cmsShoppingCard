package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.UserRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user != null)
            return user;

        throw new UsernameNotFoundException("User having username: " + username + " was not found");

    }
}
