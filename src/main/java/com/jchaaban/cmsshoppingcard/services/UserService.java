package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.UserRepository;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.utilities.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user){
        repository.save(user);
    }

    public boolean usernameExist(User user, boolean editMode){
        User existingUser = repository.findByUsername(user.getUsername());
        if (existingUser != null){
            if (editMode)
                return existingUser.getId() != user.getId();
            return true;
        }
        return false;
    }

    public boolean emailExist(User user, boolean editMode){
        User existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null){
            if (editMode)
                return existingUser.getId() != user.getId();
            return true;
        }
        return false;
    }

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

}
