package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.AppUserBalanceRecordEnum;
import com.xingchuan.charging.enums.AppUserEnum;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户余额对象 app_user_balance
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Data
@ApiModel(value = "小程序用户余额变动记录列表查询返回")
public class UserBalanceRecordListResponse {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String phone;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    /**
     * 用户类型描述
     */
    @ApiModelProperty(value = "用户类型描述")
    private String userTypeDesc;

    /**
     * 操作类型（0：充值;1：消费;2：提现,3：订单退款,4:企业余额分配）
     */
    @ApiModelProperty(value = "操作类型")
    private Integer type;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型描述")
    private String typeDesc;

    /**
     * 场站名称
     */
    @ApiModelProperty(value = "场站名称")
    private String stationName;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String tradeNo;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;

    /**
     * 本次剩余余额
     */
    @ApiModelProperty(value = "本次剩余余额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal lastAmount;

    /**
     * 可退款金额
     */
    @ApiModelProperty(value = "可退款金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal refundableAmount;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public String getTypeDesc() {
        return typeDesc = AppUserBalanceRecordEnum.getDesc(type);
    }

    public String getUserTypeDesc() {
        return userTypeDesc = AppUserEnum.getName(userType);
    }

}
