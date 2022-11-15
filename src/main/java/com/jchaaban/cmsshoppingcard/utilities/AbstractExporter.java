package com.jchaaban.cmsshoppingcard.utilities;

import com.jchaaban.cmsshoppingcard.models.data.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class AbstractExporter {

    public void setResponseHeader(HttpServletResponse response, String contentType, String extension) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = "users_" + timestamp + extension;
        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey,headerValue);
    }

    abstract public void export(List<User> userList, HttpServletResponse response) throws IOException;
}
