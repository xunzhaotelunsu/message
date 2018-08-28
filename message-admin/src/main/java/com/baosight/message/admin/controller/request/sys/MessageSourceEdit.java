package com.baosight.message.admin.controller.request.sys;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class MessageSourceEdit {

    @NotEmpty(message = "业务模块代码必填")
    String sourceCode;

    @NotEmpty(message = "业务模块名称必填")
    String sourceName;

    String password;
}
