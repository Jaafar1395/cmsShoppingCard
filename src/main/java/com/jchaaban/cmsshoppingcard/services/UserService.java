package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.UserRepository;
import com.jchaaban.cmsshoppingcard.models.data.Address;
import com.jchaaban.cmsshoppingcard.models.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


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

    public void saveNewUser(User user, Address address) {
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAddress(address);
        userRepository.save(user);
    }

    public User getUserHavingUsername(String username) {
        User user = userRepository.findByUsername(username);
        System.out.println("UUUUUUUUUUUUU "  + user.getUsername());
        return user;
    }

    public List<User> findAllByOrderByUsernameAsc(){
        return userRepository.findAllByOrderByUsernameAsc();
    }

    public Long count() {
        return userRepository.count();
    }
}
