package com.jchaaban.cmsshoppingcard.security;

import com.jchaaban.cmsshoppingcard.models.AdminRepository;
import com.jchaaban.cmsshoppingcard.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectURL;

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        if (userDetailsService.isAdmin(principal.getUsername()))
            redirectURL = "/admin/products";
        else if (request.getSession().getAttribute("card") != null)
            redirectURL = "/card/details";
        else  redirectURL = "/";

        response.sendRedirect(redirectURL);
    }

}
