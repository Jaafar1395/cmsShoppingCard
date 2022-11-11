package com.jchaaban.cmsshoppingcard.security;

import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;


    @GetMapping
    public String register(User user){
        return "register";
    }


    @PostMapping
    public String register(@Valid User user, BindingResult bindingResult, Model model)
            throws IOException {

        if (bindingResult.hasErrors())
            return "register";

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordMatchProblem", "Passwords are not matching");
            return "register";
        }

        if (userService.emailExist(user,false)) {
            model.addAttribute("existingEmailProblem","The email you entered is already used");
            return "register";
        }

        userService.saveNewUser(user);
        return "redirect:/login";
    }



}
