package com.jchaaban.cmsshoppingcard.controllers.admin;

import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.services.UserService;
import com.jchaaban.cmsshoppingcard.utilities.UserCsvExporter;
import com.jchaaban.cmsshoppingcard.utilities.UserExelExporter;
import com.jchaaban.cmsshoppingcard.utilities.UserPdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String users(Model model){
        List<User> users = userService.findAllByOrderByIsAdminDesc();
        model.addAttribute("users",users);

        return "admin/users/index";
    }

    @GetMapping("/{id}/flipEnabledStatus")
    @ResponseBody
    public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id){
        User user = userService.findById(id);
        boolean isEnabled = user.isEnabled();
        user.setEnabled(!isEnabled);
        userService.save(user);
        return String.valueOf(!isEnabled);
    }

    @GetMapping("/exportPdf")
    public void exportPdf(HttpServletResponse response) throws IOException {
        List<User> users = userService.findAllByOrderByIsAdminDesc();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(users, response);
    }

    @GetMapping("/exportExel")
    public void exportExel(HttpServletResponse response) throws IOException {
        List<User> users = userService.findAllByOrderByIsAdminDesc();
        UserExelExporter exporter = new UserExelExporter();
        exporter.export(users, response);
    }

    @GetMapping("/exportCsv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        List<User> users = userService.findAllByOrderByIsAdminDesc();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(users, response);
    }



}
