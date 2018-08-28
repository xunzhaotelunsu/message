package com.baosight.message.admin.controller.request.biz;

import com.baosight.message.core.MessageConstants;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class MessageBizTypeAdd {

    @NotEmpty(message = "业务系统代码必填")
    String sourceCode;

    @NotEmpty(message = "消息类型代码必填")
    String messageBizType;

    @NotEmpty(message = "消息类型名称必填")
    String typeName;

    @NotEmpty(message = "消息发送类型必选")
    List<String> sendTypes;

    int rateLimit = -1;

    String limitUnit = MessageConstants.MINUTE;
}
