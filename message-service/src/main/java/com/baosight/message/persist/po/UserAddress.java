package com.baosight.message.persist.po;

import lombok.Data;

/**
 * Created by yang on 2018/6/25.
 */
@Data
public class UserAddress {

    String userId;

    String messageSendType;

    String address;

    String createTime;
}
