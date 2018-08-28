package com.baosight.message.admin.controller.request.server;

import lombok.Data;

@Data
public class SetActive {

    String serverCode;

    boolean isActive;
}
