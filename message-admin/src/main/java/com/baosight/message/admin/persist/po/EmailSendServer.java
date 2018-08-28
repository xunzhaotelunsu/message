package com.baosight.message.admin.persist.po;

import lombok.Data;

@Data
public class EmailSendServer {

    String serverCode;

    String host;

    int port =25;

    String username;

    String password;

    String personal;

    String active;

    int priority;

    String createTime;

}
