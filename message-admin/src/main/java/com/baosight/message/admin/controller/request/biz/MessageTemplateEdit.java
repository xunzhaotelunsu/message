package com.baosight.message.admin.controller.request.biz;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class MessageTemplateEdit {

    @NotEmpty(message = "消息业务类型非空")
    String messageBizType;

    @NotEmpty(message = "消息发送类型非空")
    String messageSendType;

    String template;
}
