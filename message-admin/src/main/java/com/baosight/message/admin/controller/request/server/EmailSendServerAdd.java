package com.baosight.message.admin.controller.request.server;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

@Data
public class EmailSendServerAdd {

    @NotEmpty(message = "邮件服务代码必填")
    String serverCode;

    @NotEmpty(message = "smtp服务器地址必填")
    String host;

    @Min(value = 0,message = "请检查端口值")
    int port = 25;

    @NotEmpty(message = "邮件服务器登录名必填")
    String username;

    @NotEmpty(message = "邮件服务器密码必填")
    String password;

    @NotEmpty(message = "邮件服务别名必填")
    String personal;

    @NotEmpty(message = "是否启用必选")
    String active;

    @Min(value = 0,message = "请检查优先级值")
    int priority;
}
