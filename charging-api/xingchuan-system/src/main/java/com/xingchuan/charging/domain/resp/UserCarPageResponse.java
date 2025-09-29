package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.AppUserCarAuditVerifiedEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户车辆列表返回信息")
public class UserCarPageResponse {

    /**
     * 用户车辆id
     */
    @ApiModelProperty(value = "用户车辆id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String phone;

    /**
     * 用户openId
     */
    @ApiModelProperty(value = "用户openId")
    private String openId;

    /**
     * 车牌号（保证唯一）
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    /**
     * 车辆用途（0:私家车、1:网约车、2:出租车、3:物流车）
     */
    @ApiModelProperty(value = "车辆用途")
    private String usage;

    /**
     * VIN车架号（保证唯一）
     */
    @ApiModelProperty(value = "VIN车架号")
    private String vin;

    /**
     * 品牌型号
     */
    @ApiModelProperty(value = "品牌型号")
    private String model;

    /**
     * 行驶证照片车辆信息页图片
     */
    @ApiModelProperty(value = "行驶证照片车辆信息页图片")
    private String image;

    /**
     * 是否默认车辆（0：否；1：是）
     */
    @ApiModelProperty(value = "是否默认车辆")
    private String defaultVehicle;

    /**
     * 是否已认证(0:未认证，1：审核中，2：已认证，3：认证失败)
     */
    @ApiModelProperty(value = "认证状态(0:未认证，1：审核中，2：已认证，3：认证失败)")
    private Integer verified;

    /**
     * 是否已认证
     */
    @ApiModelProperty(value = "认证状态描述")
    private String verifiedDesc;

    public String getVerifiedDesc() {
        return verifiedDesc = AppUserCarAuditVerifiedEnum.getDesc(verified);
    }
}
