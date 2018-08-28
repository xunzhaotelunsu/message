package com.baosight.message.admin.persist.dto;

import com.baosight.message.admin.persist.po.SendType;
import lombok.Data;

@Data
public class SendTypeDetail extends SendType {

    boolean use;

}
