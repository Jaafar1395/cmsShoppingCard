package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.UserRepository;
import com.jchaaban.cmsshoppingcard.models.data.Address;
import com.jchaaban.cmsshoppingcard.models.data.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void save(User user){
        userRepository.save(user);
    }

    public void saveNewUser(User user, Address address) {
        user.setEnabled(true);
        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAddress(address);
        save(user);
    }

    public User getUserHavingUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllByOrderByIsAdminDesc(){
        return userRepository.findAllByOrderByIsAdminDesc();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

    public Long count() {
        return userRepository.count();
    }
}
