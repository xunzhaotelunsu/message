package com.baosight.message.core;

/**
 * Created by yang on 2018/6/19.
 */
public class MessageConstants {

    private MessageConstants() {

    }

    /**
     * Message 预处理状态
     */
    //预处理失败
    public static final String PRE_FAILED = "failed";

    //预处理成功
    public static final String PRE_SUCCESS = "success";

    /**
     * Message 发送状态
     */
    //发送就绪
    public static final String FOR_SEND = "forSend";

    //发送成功
    public static final String SEND_SUCCESS = "success";

    //发送失败
    public static final String SEND_FAILED = "failed";


    /**
     * send类型
     */
    //短信
    public static final String SMS = "sms";

    //邮件
    public static final String EMAIL = "email";

    //即时通信
    public static final String IM = "im";

    //移动客户端
    public static final String MOBILE = "mobile";

    //微信
    public static final String WECHAT = "wechat";

    /**
     * receiver类型
     */
    //单人
    public static final String SINGLE = "single";

    //群组
    public static final String GROUP = "group";

    /**
     * 队列名称
     */
    //消息收集
    public static final String MESSAGE_QUEUE = "message";

    //手动重试
    public static final String MANUAL_QUEUE = "manual";

    /**
     * 时间单位
     */
    public static final String MINUTE = "分钟";

    public static final String HOUR = "小时";

    public static final String DAY = "天";


}
