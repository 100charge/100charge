package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.RefundStatusEnum;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户提现审核对象 user_withdrawal_request
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "用户提现审核记录列表返回")
public class UserWithdrawalListResponse {

    /**
     * 提现记录明细列表
     */
    @ApiModelProperty(value = "提现记录明细列表")
    List<WithdrawalDetailResponse> detailResponseList;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户openId
     */
    @ApiModelProperty(value = "用户openId")
    private String openId;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String phone;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String tradeNo;
    /**
     * 操作类型（0：小程序用户;1：运营商）
     */
    @ApiModelProperty(value = "操作类型（0：小程序用户;1：运营商）")
    private Integer type;
    /**
     * 操作类型描述（0：小程序用户;1：运营商）
     */
    @ApiModelProperty(value = "操作类型描述")
    private String typeDesc;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;
    /**
     * 审核状态（0：待审核,1：审核通过,2：审核驳回）
     */
    @ApiModelProperty(value = "审核状态（0：待审核,1：审核通过,2：审核驳回）")
    private Integer status;
    /**
     * 审核状态描述（0：待审核,1：审核通过,2：审核驳回）
     */
    @ApiModelProperty(value = "审核状态描述")
    private String statusDesc;
    /**
     * 退款状态（0：待退款,1：退款中,2：退款完成）
     */
    @ApiModelProperty(value = "退款状态（0：待退款,1：退款中,2：退款完成）")
    private Integer refundStatus;
    /**
     * 退款状态描述（0：待退款,1：退款中,2：退款完成）
     */
    @ApiModelProperty(value = "退款状态描述")
    private String refundStatusDesc;
    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private String approveBy;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getStatusDesc() {
        return statusDesc = RefundStatusEnum.ReviewStatusEnum.getDescByCode(status);
    }

    public String getTypeDesc() {
        return "小程序用户";
    }

    public String getRefundStatusDesc() {
        return refundStatusDesc = RefundStatusEnum.getDescByCode(refundStatus);
    }
}
