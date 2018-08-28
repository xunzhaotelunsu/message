package com.baosight.message.core;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by yang on 2018/6/26.
 */
@Data
public class AttachmentInfo {

    /**
     * 附件文件名
     */
    @NotEmpty
    String attName;

    /**
     * 附件下载路径
     */
    @NotEmpty
    String attUrl;

}
