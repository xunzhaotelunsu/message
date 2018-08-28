package com.baosight.message.admin.persist.service;

import com.baosight.message.admin.persist.mapper.SendTypeMapper;
import com.baosight.message.admin.persist.po.SendType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendTypeService {

    @Autowired
    SendTypeMapper sendTypeMapper;

    public List<SendType> getSendTypes(){
        return sendTypeMapper.sendTypes();
    }
}
