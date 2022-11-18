package com.jchaaban.cmsshoppingcard.controllers.admin;

import com.jchaaban.cmsshoppingcard.config.CmsShoppingCardProps;
import com.jchaaban.cmsshoppingcard.models.data.User;
import com.jchaaban.cmsshoppingcard.services.UserService;
import com.jchaaban.cmsshoppingcard.utilities.UserCsvExporter;
import com.jchaaban.cmsshoppingcard.utilities.UserExelExporter;
import com.jchaaban.cmsshoppingcard.utilities.UserPdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CmsShoppingCardProps properties;

    @GetMapping
    public String users(Model model, @RequestParam(value = "page", required = false) Integer pageNum){
        int page = pageNum == null ? 0 : pageNum;
        int perPage = properties.getUsersPageSize();
        Pageable pageable = PageRequest.of(page,perPage);
        Page<User> users = userService.findAllByOrderByIsAdminDesc(pageable);
        model.addAttribute("users",users);
        Long count = userService.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
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
