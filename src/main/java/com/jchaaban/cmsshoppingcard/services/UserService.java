package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.UserRepository;
import com.jchaaban.cmsshoppingcard.models.data.Address;
import com.jchaaban.cmsshoppingcard.models.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public void saveExistingUser(User user,User existingUser, Address address) {
        user.setEnabled(true);
        user.setAdmin(existingUser.isAdmin());
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

    public boolean validateUserForm(User user, BindingResult userBindingResult, BindingResult addressBindingResult,
                                    Model model, boolean isEditMode){

        if (addressBindingResult.hasErrors() || userBindingResult.hasErrors())
            return false;


        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordMatchProblem", "Passwords are not matching");
            return false;
        }

        if (emailExist(user,isEditMode)) {
            model.addAttribute("existingEmailProblem","The email you entered is already used");
            return false;
        }

        if (usernameExist(user,isEditMode)) {
            model.addAttribute("existingUsernameProblem","The username you entered is already used");
            return false;
        }

        return true;
    }

    public Long count() {
        return userRepository.count();
    }

    private boolean usernameExist(User user, boolean isEditMode){
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null){
            if (isEditMode)
                return existingUser.getId() != user.getId();
            return true;
        }
        return false;
    }

    private boolean emailExist(User user, boolean isEditMode){
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null){
            if (isEditMode)
                return existingUser.getId() != user.getId();
            return true;
        }
        return false;
    }
}
