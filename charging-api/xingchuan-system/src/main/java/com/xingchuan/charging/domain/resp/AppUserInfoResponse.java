package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 小程序我的页面-用户信息
 *
 * @author ruoyi
 */
@Data
@ApiModel("用户信息")
public class AppUserInfoResponse {

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String userName;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phoneNumber;

    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    private String plateNo;

    /**
     * 车辆用途
     */
    @ApiModelProperty("车辆用途")
    private String usage;

    /**
     * 账户类型（0：企业客户；1：微信；2：支付宝）
     */
    @ApiModelProperty("账户类型（0：企业客户；1：微信；2：支付宝）")
    private Integer type;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    private String avatar;

    /**
     * 余额
     */
    @ApiModelProperty("余额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal balance;

    /**
     * 优惠券数量
     */
    @ApiModelProperty("优惠券数量")
    private Integer coupon;

}
