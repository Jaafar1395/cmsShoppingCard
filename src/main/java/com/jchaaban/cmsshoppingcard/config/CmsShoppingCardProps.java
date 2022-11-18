package com.jchaaban.cmsshoppingcard.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Component("cmsShoppingCard")
@Data
@ConfigurationProperties(prefix = "cmsshippingcard")
@Validated
public class CmsShoppingCardProps {

    @Min(value = 5, message = "Orders page size must be between 5 and 25")
    @Max(value = 25, message = "Orders page size must be between 5 and 25")
    private int ordersPageSize;

    @Min(value = 5, message = "Users page size must be between 5 and 25")
    @Max(value = 25, message = "Users page size must be between 5 and 25")
    private int usersPageSize;

    @Min(value = 5, message = "Standard page size must be between 5 and 25")
    @Max(value = 25, message = "Standard page size must be between 5 and 25")
    private int standardPageSize;

    private String mail;

    private String imgUploadDir;

    private List<String> acceptedImgFormats;

}
