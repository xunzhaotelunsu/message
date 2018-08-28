package com.baosight.message.rest.controller;

import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yang on 2018/7/16.
 */
@RestController
@Slf4j
public class CollectController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 收集业务系统源信息，发送至队列
     * @param message
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/collectMessage", produces = "application/json", consumes = "application/json")
    public CollectResult collectMessage(@RequestBody @Valid Message message, BindingResult bindingResult){
        CollectResult collectResult = new CollectResult();
        if(bindingResult.hasErrors()){
            collectResult.setStatusCode(0);
            collectResult.setMsg(bindingResult.getFieldError().getDefaultMessage());
        }else{
            try{
                String tokenSource = SecurityContextHolder.getContext().getAuthentication().getName();
                if(!tokenSource.equals(message.getSourceCode())){
                    collectResult.setStatusCode(-1);
                    collectResult.setMsg("sourceCode doesn't match!");
                }else{
                    String messageId = UUID.randomUUID().toString().replaceAll("-","");
                    message.setMessageId(messageId);
                    message.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    rabbitTemplate.convertAndSend(MessageConstants.MESSAGE_QUEUE, message);
                    collectResult.setStatusCode(1);
                    collectResult.setMsg(messageId);
                }
            }catch (Exception e){
                log.error(e.getMessage(), e);
                collectResult.setStatusCode(-1);
                collectResult.setMsg(e.getMessage());
            }
        }
        return collectResult;
    }

    @Data
    class CollectResult{

        int statusCode;

        String msg;
    }
}
