package com.baosight.message.handler.pre;

import com.baosight.message.core.Message;
import com.baosight.message.core.MessageConstants;
import com.baosight.message.core.Receiver;
import com.baosight.message.handler.MessagePreHandleException;
import com.baosight.message.handler.MessagePreHandler;
import com.baosight.message.persist.mapper.UserAddressMapper;
import com.baosight.message.persist.po.UserAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2018/6/25.
 */
@Component
@Order(3)
public class ReceiverHandler implements MessagePreHandler{

    @Autowired
    UserAddressMapper userAddressMapper;

    /**
     * 预处理消息
     * 处理消息接受者（读取本地数据库或者与用户管理系统对接）
     * @param message
     * @return
     */
    @Override
    public Message handleMessage(Message message) throws MessagePreHandleException {
        List<Receiver> receiverList = new ArrayList<>();
        List<String> messageSendTypeList = message.getMessageSendType();
        if (MessageConstants.SINGLE.equals(message.getMessageReceiverType())) {
            //单人
            for(String messageSendType: messageSendTypeList){
                UserAddress userAddress = userAddressMapper.getUserAddress(message.getMessageReceiver(), messageSendType);
                if(ObjectUtils.isEmpty(userAddress) || StringUtils.isEmpty(userAddress.getAddress())){
                    throw new MessagePreHandleException(message.getMessageReceiver() + ":消息接受者地址错误");
                }else{
                    Receiver receiver = new Receiver();
                    receiver.setReceiverId(message.getMessageReceiver());
                    receiver.setMessageSendType(messageSendType);
                    receiver.setReceiverAddress(userAddress.getAddress());
                    receiverList.add(receiver);
                }
            }
        }else if(MessageConstants.GROUP.equals(message.getMessageReceiverType())){
            //群组
            for(String messageSendType: messageSendTypeList){
                List<UserAddress> userAddressList = userAddressMapper.getGroupAddress(message.getMessageReceiver(), messageSendType);
                if(CollectionUtils.isEmpty(userAddressList)){
                    continue;
                    //throw new MessagePreHandleException(message.getMessageReceiver() + ":消息接受者地址错误");
                }else{
                    for(UserAddress userAddress: userAddressList){
                        Receiver receiver = new Receiver();
                        receiver.setReceiverId(userAddress.getUserId());
                        receiver.setMessageSendType(messageSendType);
                        receiver.setReceiverAddress(userAddress.getAddress());
                        receiverList.add(receiver);
                    }
                }
            }
        }else{
            throw new MessagePreHandleException("消息接收者类型错误");
        }
        if(CollectionUtils.isEmpty(receiverList)){
            throw new MessagePreHandleException("消息接受者地址错误");
        }
        message.setReceiverList(receiverList);
        return message;
    }
}
