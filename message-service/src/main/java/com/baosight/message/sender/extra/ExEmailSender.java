package com.baosight.message.sender.extra;

import lombok.Data;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by yang on 2018/7/11.
 * 扩展邮件发送
 */
@Data
public class ExEmailSender extends JavaMailSenderImpl implements Comparable<ExEmailSender>{

    /**
     * 邮件服务代码
     */
    String serverCode;

    /**
     * 发送邮箱别名
     */
    String personal;

    /**
     * 优先级
     */
    int priority;

    public ExEmailSender(){
        super();
    }

    @Override
    public int compareTo(ExEmailSender exEmailSender){
        if(this.priority > exEmailSender.priority){
            return 1;
        }else{
            return -1;
        }
    }
}
