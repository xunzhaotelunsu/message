package com.baosight.message.core;

import lombok.Data;

@Data
public abstract class AbstractResponse {

    protected boolean status;

    protected String msg;
}
