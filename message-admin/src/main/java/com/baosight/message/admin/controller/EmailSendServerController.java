package com.baosight.message.admin.controller;

import com.baosight.message.admin.RefreshService;
import com.baosight.message.admin.controller.request.server.EmailSendServerAdd;
import com.baosight.message.admin.controller.request.server.EmailSendServerPageQuery;
import com.baosight.message.admin.controller.request.server.SetActive;
import com.baosight.message.admin.controller.response.ServerResponse;
import com.baosight.message.admin.persist.po.EmailSendServer;
import com.baosight.message.admin.persist.service.EmailSendServerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/server/email")
public class EmailSendServerController {

    @Autowired
    EmailSendServerService emailSendServerService;

    @Autowired
    RefreshService refreshService;

    @RequestMapping("/servers")
    public ModelAndView emailServers(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/server/email/servers");
        return mv;
    }

    @RequestMapping("/serverAdd")
    public ModelAndView serverAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/server/email/serverAdd");
        return mv;
    }

    @RequestMapping("/serverEdit")
    public ModelAndView serverEdit(@RequestParam(name = "serverCode") String serverCode){
        ModelAndView mv = new ModelAndView();
        EmailSendServer emailSendServer = emailSendServerService.getEmailSendServer(serverCode);
        mv.setViewName("/server/email/serverEdit");
        mv.addObject("server", emailSendServer);
        return mv;
    }

    @RequestMapping(value = "/addServer", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ServerResponse addServer(@RequestBody @Valid EmailSendServerAdd emailSendServerAdd, BindingResult bindingResult){
        ServerResponse serverResponse = new ServerResponse();
        if(bindingResult.hasErrors()){
            serverResponse.setStatus(false);
            serverResponse.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return serverResponse;
        }
        if(emailSendServerService.check(emailSendServerAdd.getServerCode())){
            EmailSendServer emailSendServer = new EmailSendServer();
            BeanUtils.copyProperties(emailSendServerAdd, emailSendServer);
            emailSendServer.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            emailSendServerService.insert(emailSendServer);
            serverResponse.setStatus(true);
            serverResponse.setServerCode(emailSendServer.getServerCode());
        }else{
            serverResponse.setStatus(false);
            serverResponse.setMsg("服务代码已被使用");
            return serverResponse;
        }
        refreshService.refreshEmailServers();
        return serverResponse;
    }

    @RequestMapping(value = "/editServer", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ServerResponse editServer(@RequestBody @Valid EmailSendServerAdd emailSendServerAdd, BindingResult bindingResult){
        ServerResponse serverResponse = new ServerResponse();
        if(bindingResult.hasErrors()){
            serverResponse.setStatus(false);
            serverResponse.setMsg(bindingResult.getFieldError().getDefaultMessage());
            return serverResponse;
        }
        EmailSendServer emailSendServer = new EmailSendServer();
        BeanUtils.copyProperties(emailSendServerAdd, emailSendServer);
        emailSendServerService.update(emailSendServer);
        serverResponse.setStatus(true);
        serverResponse.setServerCode(emailSendServer.getServerCode());
        refreshService.refreshEmailServers();
        return serverResponse;
    }

    @RequestMapping(value = "/setActive", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ServerResponse setActive(@RequestBody SetActive setActive){
        ServerResponse serverResponse = new ServerResponse();
        emailSendServerService.setActive(setActive.getServerCode(), ObjectUtils.getDisplayString(setActive.isActive()));
        serverResponse.setStatus(true);
        serverResponse.setServerCode(setActive.getServerCode());
        refreshService.refreshEmailServers();
        return serverResponse;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ServerResponse delete(@RequestParam(name = "serverCode") String serverCode){
        ServerResponse serverResponse = new ServerResponse();
        emailSendServerService.delete(serverCode);
        serverResponse.setStatus(true);
        serverResponse.setServerCode(serverCode);
        refreshService.refreshEmailServers();
        return serverResponse;
    }

    @RequestMapping(value = "/serverQuery", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> serverQuery(EmailSendServerPageQuery emailSendServerPageQuery){
        PageHelper.startPage(emailSendServerPageQuery.getPage(), emailSendServerPageQuery.getLimit());
        List<EmailSendServer> list = emailSendServerService.queryEmailSendServer(emailSendServerPageQuery.toHashMap());
        PageInfo<EmailSendServer> emailSendServerPageInfo = new PageInfo<>(list);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", emailSendServerPageInfo.getTotal());
        result.put("data", emailSendServerPageInfo.getList());
        return result;
    }
}
