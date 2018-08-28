package com.baosight.message;

import com.baosight.message.sender.holder.ExEmailSenderHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshController {

    @Autowired
    ExEmailSenderHolder exEmailSenderHolder;

    @PostMapping("/refreshEmailServer")
    public String refreshEmailServer(){
        exEmailSenderHolder.refresh();
        return "refresh email server";
    }
}
