package com.baosight.message.admin.controller.request.sys;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class MessageSourceAdd {

    @NotEmpty(message = "业务模块代码必填")
    String sourceCode;

    @NotEmpty(message = "业务模块名称必填")
    String sourceName;

    @NotEmpty(message = "密码必填")
    @Length(min = 6, max = 12, message = "密码必须6~12位")
    String password;
}
