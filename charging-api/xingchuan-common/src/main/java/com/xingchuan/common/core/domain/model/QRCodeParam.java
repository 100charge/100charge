package com.xingchuan.common.core.domain.model;

import lombok.Data;

/**
 * 二维码参数类
 */
@Data
public class QRCodeParam {

    /**
     * 内容
     */
    private String content;

    /**
     * 二维码名称
     */
    private String name;

}
