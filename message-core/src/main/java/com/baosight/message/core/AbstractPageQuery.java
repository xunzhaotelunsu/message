package com.baosight.message.core;

import lombok.Data;

@Data
public abstract class AbstractPageQuery {

    /**
     * 页码数
     */
    protected int page = 1;

    /**
     * 每页数
     */
    protected int limit = 10;
}
