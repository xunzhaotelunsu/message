package com.baosight.message.admin.persist.dto;

import com.baosight.message.admin.persist.po.MessageBizType;
import com.baosight.message.admin.persist.po.SendType;
import lombok.Data;

import java.util.List;

@Data
public class MessageBizTypeDetail extends MessageBizType {

    String sourceName;

    List<SendType> sendTypes;

}
