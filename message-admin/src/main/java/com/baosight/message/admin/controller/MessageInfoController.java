package com.baosight.message.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baosight.message.admin.controller.request.info.MessageInfoPageQuery;
import com.baosight.message.admin.controller.request.info.MessageSendPageQuery;
import com.baosight.message.admin.controller.request.info.ManualRetryInfo;
import com.baosight.message.admin.persist.po.MessageInfo;
import com.baosight.message.admin.persist.po.MessageSend;
import com.baosight.message.admin.persist.po.SendType;
import com.baosight.message.admin.persist.service.MessageInfoService;
import com.baosight.message.admin.persist.service.MessageSendService;
import com.baosight.message.admin.persist.service.SendTypeService;
import com.baosight.message.core.AttachmentInfo;
import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.util.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/info")
@Slf4j
public class MessageInfoController {

    @Autowired
    MessageInfoService messageInfoService;

    @Autowired
    MessageSendService messageSendService;

    @Autowired
    SendTypeService sendTypeService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/messages")
    public ModelAndView messages(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/info/messages");
        return mv;
    }

    @RequestMapping(value = "/pageQuery", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> messagesQuery(MessageInfoPageQuery messageInfoPageQuery){
        PageHelper.startPage(messageInfoPageQuery.getPage(), messageInfoPageQuery.getLimit());
        List<MessageInfo> list = messageInfoService.queryMessageInfo(messageInfoPageQuery.toHashMap());
        PageInfo<MessageInfo> messageInfoPageInfo = new PageInfo<>(list);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", messageInfoPageInfo.getTotal());
        result.put("data", messageInfoPageInfo.getList());
        return result;
    }

    @RequestMapping("/messageDetail")
    public ModelAndView messageDetail(@RequestParam(name = "messageId") String messageId){
        ModelAndView mv = new ModelAndView();
        MessageInfo messageInfo = messageInfoService.getMessageInfo(messageId);
        messageInfo.setContent(JSONUtil.prettyJson(messageInfo.getContent()));
        messageInfo.setAttachments(JSONUtil.prettyJson(messageInfo.getAttachments()));
        messageInfo.setExtraInfo(JSONUtil.prettyJson(messageInfo.getExtraInfo()));
        mv.setViewName("/info/messageDetail");
        mv.addObject("messageInfo", messageInfo);
        return mv;
    }

    @RequestMapping("/messageSend")
    public ModelAndView messageSend(@RequestParam(name = "messageId") String messageId){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/info/messageSend");
        List<SendType> sendTypes = sendTypeService.getSendTypes();
        mv.addObject("messageId", messageId);
        mv.addObject("sendTypes", sendTypes);
        return mv;
    }

    @RequestMapping(value = "/sendQuery", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> messageSendQuery(MessageSendPageQuery messageSendPageQuery){
        PageHelper.startPage(messageSendPageQuery.getPage(), messageSendPageQuery.getLimit());
        List<MessageSend> list = messageSendService.queryMessageSend(messageSendPageQuery.toHashMap());
        PageInfo<MessageSend> messageSendPageInfo = new PageInfo<>(list);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", messageSendPageInfo.getTotal());
        result.put("data", messageSendPageInfo.getList());
        return result;
    }


    @RequestMapping(value = "/manualRetry", method = RequestMethod.POST)
    @ResponseBody
    public String manualRetry(@RequestBody ManualRetryInfo manualRetryInfo){
        MessageInfo messageInfo = messageInfoService.getMessageInfo(manualRetryInfo.getMessageId());
        Message single = new Message();
        BeanUtils.copyProperties(messageInfo, single, "attachments","extraInfo");
        single.setMessageReceiver(manualRetryInfo.getReceiverId());
        single.setMessageReceiverAddress(manualRetryInfo.getReceiverAddress());
        single.setMessageReceiverType(MessageConstants.SINGLE);
        List<String> sendType = new ArrayList<>();
        sendType.add(manualRetryInfo.getMessageSendType());
        single.setMessageSendType(sendType);
        List<AttachmentInfo> attachmentInfos = JSON.parseObject(messageInfo.getAttachments(), new TypeReference<List<AttachmentInfo>>(){});
        single.setAttachments(attachmentInfos);
        Map<String, Object> extraInfo = JSON.parseObject(messageInfo.getExtraInfo(), new TypeReference<Map<String, Object>>(){});
        single.setExtraInfo(extraInfo);
        rabbitTemplate.convertAndSend(MessageConstants.MANUAL_QUEUE, single);
        return "已发送至重试队列";
    }
}
