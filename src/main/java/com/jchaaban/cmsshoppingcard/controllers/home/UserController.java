package com.jchaaban.cmsshoppingcard.controllers.home;

import com.jchaaban.cmsshoppingcard.models.data.Address;
import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public String user(@PathVariable(name = "id") Integer id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("address", user.getAddress());

        return "editUser";
    }

    @PostMapping("/edit")
    public String edit(@Valid User user, BindingResult userBindingResult,
                           @Valid Address address, BindingResult addressBindingResult, Model model) {

        User existingUser = userService.findById(user.getId());

        boolean formHasErrors = userService.validateUserForm(user,userBindingResult,addressBindingResult,model,
                true);

        if (!formHasErrors)
            return "editUser";

        userService.saveExistingUser(user,existingUser,address);

        return "redirect:/users/" + existingUser.getId();
    }
}
