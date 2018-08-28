package com.baosight.message.core;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2018/6/19.
 */
@Data
public class Message {

    /**
     * 消息id，主键
     */
    String messageId;

    /**
     * 来源业务系统code
     */
    @NotEmpty(message = "来源业务系统code不能为空")
    String sourceCode;

    /**
     * 发送人id
     */
    String messageSender;

    /**
     * 消息业务类型
     */
    @NotEmpty(message = "消息业务类型不能为空")
    String messageBizType;

    /**
     * 消息业务主键
     */
    String messageBizId;

    /**
     * 消息发送类型（预处理设置,不持久化）
     */
    List<String> messageSendType;

    /**
     * 消息接收人或群组id
     */
    @NotEmpty(message = "消息接收者不能为空")
    String messageReceiver;

    /**
     * 消息接收者类型（single/group)
     */
    @NotEmpty(message = "消息接收者类型不能为空")
    String messageReceiverType;

    /**
     * 消息接受者地址（可空）
     */
    String messageReceiverAddress;

    /**
     * 消息接收人列表（预处理设置,不持久化）
     */
    List<Receiver> receiverList;

    /**
     * 标题
     */
    String title;

    /**
     * 内容（文本或者模板填充内容）
     */
    @NotEmpty(message = "内容不能为空")
    String content;

    /**
     * 预处理状态（预处理设置）
     */
    String preStatus;

    /**
     * 备注
     */
    String remark;

    /**
     * 优先级
     */
    String privilege;

    /**
     * 创建时间
     */
    String createTime;

    /**
     * 附件列表
     */
    List<AttachmentInfo> attachments;

    /**
     * 扩展参数
     */
    Map<String, Object> extraInfo;


}
