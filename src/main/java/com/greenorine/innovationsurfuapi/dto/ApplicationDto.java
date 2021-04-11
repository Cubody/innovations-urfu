package com.greenorine.innovationsurfuapi.dto;

import com.greenorine.innovationsurfuapi.model.Application;
import lombok.Data;

@Data
public class ApplicationDto {
    private String title;
    private String content;
    private String address;
    private String tags;
    private String contacts;
    private double price;
    private String attachments;

    public Application toApplication() {
        var app = new Application();
        return getApplication(app);
    }

    public Application toApplication(Application app) {
        return getApplication(app);
    }

    private Application getApplication(Application app) {
        app.setTitle(title);
        app.setContent(content);
        app.setAddress(address);
        app.setTags(tags);
        app.setContacts(contacts);
        app.setPrice(price);
        app.setAttachments(attachments);
        return app;
    }
}