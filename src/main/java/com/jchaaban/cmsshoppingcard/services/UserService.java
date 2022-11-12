package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.AdminRepository;
import com.jchaaban.cmsshoppingcard.models.UserRepository;
import com.jchaaban.cmsshoppingcard.models.data.Admin;
import com.jchaaban.cmsshoppingcard.models.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean usernameExist(User user, boolean editMode){
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null){
            if (editMode)
                return existingUser.getId() != user.getId();
            return true;
        }
        return false;
    }

    public boolean emailExist(User user, boolean editMode){
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null){
            if (editMode)
                return existingUser.getId() != user.getId();
            return true;
        }
        return false;
    }

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
