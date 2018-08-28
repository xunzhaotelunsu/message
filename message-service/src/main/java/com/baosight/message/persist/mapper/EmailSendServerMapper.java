package com.baosight.message.persist.mapper;

import com.baosight.message.persist.po.EmailSendServer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by yang on 2018/7/11.
 */
@Mapper
public interface EmailSendServerMapper {

    List<EmailSendServer> getEmailSendServers();
}
