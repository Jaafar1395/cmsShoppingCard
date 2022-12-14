package com.jchaaban.cmsshoppingcard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CmsShoppingCardProps properties;

    // just to show that this works
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("home"); // just return a view
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String directoryName = properties.getImgUploadDir();
        Path userPhotosDirectory = Paths.get(directoryName);
        String userPhotosPath = userPhotosDirectory.toFile().getAbsolutePath();
        registry.addResourceHandler("/"+ directoryName + "/**" )
                .addResourceLocations("file:/" + userPhotosPath + "/");
    }
}
