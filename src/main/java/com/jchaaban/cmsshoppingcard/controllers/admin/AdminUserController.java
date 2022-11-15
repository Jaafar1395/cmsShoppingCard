package com.jchaaban.cmsshoppingcard.controllers.admin;

import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String users(Model model){
        List<User> users = userService.findAllByOrderByUsernameAsc();
        model.addAttribute("users",users);
        return "admin/users/index";
    }


}
