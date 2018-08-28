package com.baosight.message.admin.controller;

import com.baosight.message.admin.controller.request.sys.MessageSourceAdd;
import com.baosight.message.admin.controller.request.sys.MessageSourceEdit;
import com.baosight.message.admin.controller.request.sys.MessageSourcePageQuery;
import com.baosight.message.admin.controller.response.MessageSourceResponse;
import com.baosight.message.admin.persist.dto.MessageBizTypeDetail;
import com.baosight.message.admin.persist.po.MessageSource;
import com.baosight.message.admin.persist.service.MessageBizTypeService;
import com.baosight.message.admin.persist.service.MessageSourceService;
import com.baosight.message.util.SHA256Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys")
public class MessageSourceController {

    @Autowired
    MessageSourceService messageSourceService;

    @Autowired
    MessageBizTypeService messageBizTypeService;

    @RequestMapping("/sources")
    public ModelAndView messageSources(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/sys/sources");
        return mv;
    }

    @RequestMapping("/sourceAdd")
    public ModelAndView sourceAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/sys/sourceAdd");
        return mv;
    }

    @RequestMapping("/sourceEdit")
    public ModelAndView sourceEdit(@RequestParam(name = "sourceCode") String sourceCode){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/sys/sourceEdit");
        MessageSource messageSource = messageSourceService.getMessageSource(sourceCode);
        mv.addObject("sourceCode", sourceCode);
        mv.addObject("sourceName", messageSource.getSourceName());
        return mv;
    }

    @RequestMapping(value = "/sourceQuery", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> messageSourceQuery(MessageSourcePageQuery messageSourcePageQuery){
        PageHelper.startPage(messageSourcePageQuery.getPage(), messageSourcePageQuery.getLimit());
        List<MessageSource> list = messageSourceService.queryMessageSource(messageSourcePageQuery.toHashMap());
        PageInfo<MessageSource> messageSourcePageInfo = new PageInfo<>(list);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", messageSourcePageInfo.getTotal());
        result.put("data", messageSourcePageInfo.getList());
        return result;
    }

    @RequestMapping(value = "/addSource", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public MessageSourceResponse addMessageSource(@RequestBody @Valid MessageSourceAdd messageSourceAdd, BindingResult bindingResult){
        MessageSourceResponse result = new MessageSourceResponse();
        if(bindingResult.hasErrors()){
            result.setStatus(false);
            result.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return result;
        }
        if(!messageSourceService.check(messageSourceAdd.getSourceCode())){
            result.setStatus(false);
            result.setMsg("业务模块代码已存在");
            return result;
        }
        MessageSource messageSource = new MessageSource();
        BeanUtils.copyProperties(messageSourceAdd, messageSource);
        messageSource.setPassword(SHA256Util.encode(messageSourceAdd.getPassword()));
        messageSource.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        messageSourceService.addMessageSource(messageSource);
        result.setStatus(true);
        result.setSourceCode(messageSource.getSourceCode());
        return result;
    }

    @RequestMapping(value = "/editSource", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public MessageSourceResponse editMessageSource(@RequestBody @Valid MessageSourceEdit messageSourceEdit, BindingResult bindingResult){
        MessageSourceResponse result = new MessageSourceResponse();
        if(bindingResult.hasErrors()) {
            result.setStatus(false);
            result.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return result;
        }
        Map<String, String> param = new HashMap<>();
        if(StringUtils.hasText(messageSourceEdit.getPassword())){
            //需要修改密码
            String password = messageSourceEdit.getPassword();
            if(password.length()<6 || password.length()>12){
                result.setStatus(false);
                result.setMsg("密码必须6~12位");
                return result;
            }else{
                param.put("password", SHA256Util.encode(password));
            }
        }
        param.put("sourceCode", messageSourceEdit.getSourceCode());
        param.put("sourceName", messageSourceEdit.getSourceName());
        messageSourceService.modifySource(param);
        result.setStatus(true);
        result.setSourceCode(messageSourceEdit.getSourceCode());
        return result;
    }

    @RequestMapping(value = "/delSource", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public MessageSourceResponse delSource(@RequestParam(name = "sourceCode")String sourceCode){
        MessageSourceResponse result = new MessageSourceResponse();
        Map<String, String> param = new HashMap<>();
        param.put("sourceCode", sourceCode);
        List<MessageBizTypeDetail> messageBizTypes = messageBizTypeService.queryMessageBizType(param);
        messageBizTypes.stream().forEach(messageBizTypeDetail -> {
            messageBizTypeService.deleteMessageBizType(messageBizTypeDetail.getMessageBizType());
        });
        messageSourceService.deleteSource(sourceCode);
        result.setStatus(true);
        result.setSourceCode(sourceCode);
        return result;
    }
}
