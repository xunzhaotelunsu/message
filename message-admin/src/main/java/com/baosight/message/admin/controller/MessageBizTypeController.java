package com.baosight.message.admin.controller;

import com.baosight.message.admin.controller.request.biz.MessageBizTypeAdd;
import com.baosight.message.admin.controller.request.biz.MessageBizTypePageQuery;
import com.baosight.message.admin.controller.request.biz.MessageTemplateEdit;
import com.baosight.message.admin.controller.response.MessageBizTypeResponse;
import com.baosight.message.admin.persist.dto.MessageBizTypeDetail;
import com.baosight.message.admin.persist.dto.SendTypeDetail;
import com.baosight.message.admin.persist.po.MessageBizType;
import com.baosight.message.admin.persist.po.MessageTemplate;
import com.baosight.message.admin.persist.po.SendType;
import com.baosight.message.admin.persist.service.MessageBizTypeService;
import com.baosight.message.admin.persist.service.MessageSourceService;
import com.baosight.message.admin.persist.service.SendTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/biz")
public class MessageBizTypeController {

    @Autowired
    MessageBizTypeService messageBizTypeService;

    @Autowired
    MessageSourceService messageSourceService;

    @Autowired
    SendTypeService sendTypeService;

    @RequestMapping("/types")
    public ModelAndView bizTypes(@RequestParam(name = "sourceCode", required = false) String sourceCode) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/biz/types");
        mv.addObject("sourceCode", sourceCode);
        return mv;
    }

    @RequestMapping("/typeAdd")
    public ModelAndView typeAdd(@RequestParam(name = "sourceCode", required = false) String sourceCode) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/biz/typeAdd");
        mv.addObject("sourceCode", sourceCode);
        List<SendType> sendTypes = sendTypeService.getSendTypes();
        mv.addObject("sendTypes", sendTypes);
        return mv;
    }

    @RequestMapping("/typeEdit")
    public ModelAndView typeEdit(@RequestParam(name = "messageBizType") String messageBizType){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/biz/typeEdit");
        MessageBizTypeDetail messageBizTypeDetail = messageBizTypeService.getMessageBizType(messageBizType);
        mv.addObject("messageBizTypeDetail", messageBizTypeDetail);
        List<SendType> sendTypes = messageBizTypeService.getSendTypes(messageBizType);
        List<SendType> total = sendTypeService.getSendTypes();
        List<SendTypeDetail> sendTypeDetails = total.stream().map(sendType ->{
            SendTypeDetail sendTypeDetail = new SendTypeDetail();
            BeanUtils.copyProperties(sendType, sendTypeDetail);
            if(sendTypes.contains(sendType)){
                sendTypeDetail.setUse(true);
            }else{
                sendTypeDetail.setUse(false);
            }
            return sendTypeDetail;
        }).collect(Collectors.toList());
        mv.addObject("sendTypes", sendTypeDetails);
        return mv;
    }

    @RequestMapping("/tpl")
    public ModelAndView tpl(@RequestParam(name = "messageBizType") String messageBizType, @RequestParam(name = "messageSendType") String messageSendType){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/biz/tpl");
        mv.addObject("messageBizType", messageBizType);
        mv.addObject("messageSendType", messageSendType);
        mv.addObject("template", messageBizTypeService.getTemplate(messageBizType,messageSendType));
        return mv;
    }

    @RequestMapping(value = "/editTpl", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public MessageBizTypeResponse editTpl(@RequestBody @Valid MessageTemplateEdit messageTemplateEdit, BindingResult bindingResult){
        MessageBizTypeResponse result = new MessageBizTypeResponse();
        if(bindingResult.hasErrors()){
            result.setStatus(false);
            result.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return result;
        }
        MessageTemplate messageTemplate = new MessageTemplate();
        BeanUtils.copyProperties(messageTemplateEdit, messageTemplate);
        messageTemplate.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        messageBizTypeService.setMessageTemplate(messageTemplate);
        result.setStatus(true);
        result.setMessageBizType(messageTemplateEdit.getMessageBizType());
        return result;
    }

    @RequestMapping(value = "/addType", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public MessageBizTypeResponse addType(@RequestBody @Valid MessageBizTypeAdd messageBizTypeAdd, BindingResult bindingResult){
        MessageBizTypeResponse result = new MessageBizTypeResponse();
        if(bindingResult.hasErrors()){
            result.setStatus(false);
            result.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return result;
        }
        if(messageSourceService.check(messageBizTypeAdd.getSourceCode())){
            result.setStatus(false);
            result.setMsg("业务系统代码不存在");
            return result;
        }
        MessageBizType messageBizType = new MessageBizType();
        BeanUtils.copyProperties(messageBizTypeAdd, messageBizType);
        if(!messageBizTypeService.check(messageBizType.getMessageBizType())){
            result.setStatus(false);
            result.setMsg("消息类型代码重复");
            return result;
        }
        messageBizType.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        messageBizTypeService.addMessageBizType(messageBizType);
        messageBizTypeService.addMessageSendTypes(messageBizType.getMessageBizType(), messageBizTypeAdd.getSendTypes());
        result.setStatus(true);
        result.setMessageBizType(messageBizType.getMessageBizType());
        return result;
    }

    @RequestMapping(value = "/editType", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public MessageBizTypeResponse editType(@RequestBody @Valid MessageBizTypeAdd messageBizTypeAdd, BindingResult bindingResult){
        MessageBizTypeResponse result = new MessageBizTypeResponse();
        if(bindingResult.hasErrors()){
            result.setStatus(false);
            result.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return result;
        }
        MessageBizType messageBizType = new MessageBizType();
        BeanUtils.copyProperties(messageBizTypeAdd, messageBizType);
        messageBizTypeService.updateMessageBizType(messageBizType);
        //处理发送类型
        List<String> newSendTypes = messageBizTypeAdd.getSendTypes();
        List<SendType> sendTypes = messageBizTypeService.getSendTypes(messageBizTypeAdd.getMessageBizType());
        List<String> oldSendTypes = sendTypes.stream().map(sendType -> sendType.getMessageSendType()).collect(Collectors.toList());
        List<String> toAdd = new ArrayList<>(newSendTypes);;
        List<String> toDel = new ArrayList<>(oldSendTypes);
        toAdd.removeAll(oldSendTypes);
        toDel.removeAll(newSendTypes);
        messageBizTypeService.addMessageSendTypes(messageBizTypeAdd.getMessageBizType(), toAdd);
        messageBizTypeService.deleteMessageSendTypes(messageBizTypeAdd.getMessageBizType(), toDel);
        result.setStatus(true);
        result.setMessageBizType(messageBizTypeAdd.getMessageBizType());
        return result;
    }

    @RequestMapping(value = "/typeQuery", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> messageBizTypeQuery(MessageBizTypePageQuery messageBizTypePageQuery) {
        PageHelper.startPage(messageBizTypePageQuery.getPage(), messageBizTypePageQuery.getLimit());
        List<MessageBizTypeDetail> list = messageBizTypeService.queryMessageBizType(messageBizTypePageQuery.toHashMap());
        PageInfo<MessageBizTypeDetail> messageBizTypeDetailPageInfo = new PageInfo<>(list);
        List<MessageBizTypeDetail> messageBizTypeDetails = messageBizTypeDetailPageInfo.getList();
        messageBizTypeDetails.stream().parallel().forEach(detail -> detail.setSendTypes(messageBizTypeService.getSendTypes(detail.getMessageBizType())));
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", messageBizTypeDetailPageInfo.getTotal());
        result.put("data", messageBizTypeDetailPageInfo.getList());
        return result;
    }

    @RequestMapping(value = "/delType", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public MessageBizTypeResponse delType(@RequestParam(name = "messageBizType")String messageBizType){
        MessageBizTypeResponse result = new MessageBizTypeResponse();
        messageBizTypeService.deleteMessageBizType(messageBizType);
        result.setStatus(true);
        result.setMessageBizType(messageBizType);
        return result;
    }
}
