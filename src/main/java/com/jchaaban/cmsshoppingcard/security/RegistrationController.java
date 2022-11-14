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

        if (addressBindingResult.hasErrors() || userBindingResult.hasErrors())
            return "register";

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordMatchProblem", "Passwords are not matching");
            return "register";
        }

        if (userService.emailExist(user,false)) {
            model.addAttribute("existingEmailProblem","The email you entered is already used");
            return "register";
        }

        if (userService.usernameExist(user,false)) {
            model.addAttribute("existingUsernameProblem","The username you entered is already used");
            return "register";
        }

        userService.saveNewUser(user,address);
        return "redirect:/login";
    }


}
