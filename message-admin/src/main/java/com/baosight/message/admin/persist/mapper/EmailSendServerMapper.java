package com.baosight.message.admin.persist.mapper;

import com.baosight.message.admin.persist.po.EmailSendServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmailSendServerMapper {

    List<EmailSendServer> queryEmailSendServer(Map<String, String> param);

    int check(@Param(value = "serverCode") String serverCode);

    int insert(EmailSendServer emailSendServer);

    int update(EmailSendServer emailSendServer);

    int active(@Param(value = "serverCode") String serverCode);

    int inactive(@Param(value = "serverCode") String serverCode);

    int delete(@Param(value = "serverCode") String serverCode);

}
