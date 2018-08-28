package com.baosight.message.handler;

import com.alibaba.fastjson.JSON;
import com.baosight.message.core.AttachmentInfo;
import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2018/6/25.
 */
public class Test {

    public static void main(String[] args) throws IOException, MessagingException {
        Message message = new Message();
        message.setSourceCode("order");
        message.setMessageBizType("deal");
        message.setMessageReceiverType(MessageConstants.SINGLE);
        message.setMessageReceiver("cy");
        message.setTitle("订单合同提醒");
        message.setMessageBizId("DD20180719000023");
        Map<String,String> content = new HashMap<>();
        content.put("orderCode", "DD20180719000023");
        content.put("totalAmount", "320000.0");
        content.put("payDate","2018-08-19");
        List<AttachmentInfo> attachmentInfoList = new ArrayList<>();
        AttachmentInfo attachmentInfo = new AttachmentInfo();
        attachmentInfo.setAttName("合同.png");
        attachmentInfo.setAttUrl("https://www.baidu.com/img/superlogo_c4d7df0a003d3db9b65e9ef0fe6da1ec.png");
        attachmentInfoList.add(attachmentInfo);
//        AttachmentInfo attachmentInfo1 = new AttachmentInfo();
//        attachmentInfo1.setAttName("123.png");
//        attachmentInfo1.setAttUrl("http://img.ngacn.cc/attachments/mon_201806/11/-55aq9Q5-l33eZdT1kSb4-b4.png");
//        attachmentInfoList.add(attachmentInfo1);
        message.setAttachments(attachmentInfoList);
        message.setContent(JSON.toJSONString(content));
        Map<String,Object> extraInfo = new HashMap<>();
        extraInfo.put("email_serverCode", "netease");
        message.setExtraInfo(extraInfo);
        System.out.println(JSON.toJSONString(message));
    }



}
