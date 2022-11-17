package com.jchaaban.cmsshoppingcard.security;

import com.jchaaban.cmsshoppingcard.models.data.*;
import com.jchaaban.cmsshoppingcard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String register(User user, Address address){
        return "register";
    }


    @PostMapping
    public String register(@Valid User user, BindingResult userBindingResult,
                           @Valid Address address, BindingResult addressBindingResult, Model model) {

        boolean formHasErrors = userService.validateUserForm(user,userBindingResult,addressBindingResult,model,
                false);

        if (!formHasErrors)
            return "register";

        userService.saveNewUser(user,address);
        return "redirect:/login";
    }


}
