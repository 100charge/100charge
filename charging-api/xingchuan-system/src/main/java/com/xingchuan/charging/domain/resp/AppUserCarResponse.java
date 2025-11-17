package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.AppUserCarAuditVerifiedEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * app用户新增对象
 */
@Data
public class AppUserCarResponse {

    /**
     * 用户车辆id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty("id")
    private Long appCarId;

    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    private String plateNo;

    /**
     * 车辆用途
     */
    @ApiModelProperty("车辆用途")
    private String carType;

    /**
     * 是否默认车辆
     */
    @ApiModelProperty("是否默认车辆：0:否，1：是")
    private Integer isDefault;

    /**
     * 0:未认证，1：已认证
     */
    @ApiModelProperty("0:未认证，1：审核中，2：已认证，3：认证失败")
    private Integer verified;

    /**
     * vin
     */
    @ApiModelProperty("vin")
    private String vin;

    /**
     * 是否已认证
     */
    @ApiModelProperty(value = "认证状态描述")
    private String verifiedDesc;

    /**
     * 备注
     */
    private String remark;

    public String getVerifiedDesc() {
        return verifiedDesc = AppUserCarAuditVerifiedEnum.getDesc(verified);
    }

}
