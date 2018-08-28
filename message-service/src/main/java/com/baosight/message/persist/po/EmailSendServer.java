package com.baosight.message.persist.po;

import lombok.Data;

/**
 * Created by yang on 2018/7/11.
 */
@Data
public class EmailSendServer {

    String serverCode;

    String host;

    int port = 25;

    String username;

    String password;

    String personal;

    String active;

    int priority;

    String createTime;

}
