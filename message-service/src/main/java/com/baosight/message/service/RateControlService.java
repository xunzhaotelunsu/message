package com.baosight.message.service;

import com.baosight.message.core.MessageConstants;
import com.baosight.message.persist.mapper.MessageBizTypeMapper;
import com.baosight.message.persist.po.MessageBizType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 消息限流
 */
@Component
public class RateControlService {

    @Autowired
    MessageBizTypeMapper messageBizTypeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Transactional
    public boolean isRateControlled(String messageBizType, String sendType, String address){
        MessageBizType bizType = messageBizTypeMapper.getMessageBizType(messageBizType);
        int rateLimit = bizType.getRateLimit();
        if(rateLimit < 0){
            return false;
        }
        long expire = 1L;
        String limitUnit = bizType.getLimitUnit();
        if(MessageConstants.MINUTE.equals(limitUnit)){
            expire = 60L;
        }else if(MessageConstants.HOUR.equals(limitUnit)){
            expire = 3600L;
        }else if(MessageConstants.DAY.equals(limitUnit)){
            expire = 24 * 3600L;
        }
        String key = messageBizType + ":" + sendType + ":" + address;
        String value = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.hasText(value)){
            //无值，初始化
            stringRedisTemplate.opsForValue().set(key, "1", expire, TimeUnit.SECONDS);
            return false;
        }else{
            long times = Long.parseLong(value);
            if(times >= rateLimit){
                //被限流
                return true;
            }else{
                stringRedisTemplate.opsForValue().increment(key, 1L);
                return false;
            }
        }
    }
}
