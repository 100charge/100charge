package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通联-企业信息表
 */
@Data
public class QRCodeResult {

    @ApiModelProperty(value = "充电桩号")
    private String deviceNo;

    @ApiModelProperty("枪号")
    private String gunsNo;


    public QRCodeResult() {
    }

    public QRCodeResult(String deviceNo, String gunsNo) {
        this.deviceNo = deviceNo;
        this.gunsNo = gunsNo;
    }
}
