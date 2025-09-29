package com.xingchuan.charging.domain.req;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.xingchuan.charging.enums.OrderPayTypeEnum;
import com.xingchuan.charging.enums.OrderSourceEnum;
import com.xingchuan.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 开始充电请求对象
 *
 * @author ruoyi
 */
@Data
public class StartChargingRequest {

    /**
     * 场站id
     */
    @NotNull(message = "场站id不能为空")
    @ApiModelProperty(value = "场站id")
    private Long chargingId;

    /**
     * 桩编号
     */
    @NotBlank(message = "桩编号不能为空")
    @ApiModelProperty("桩编号")
    private String deviceNo;

    /**
     * 枪编号
     */
    @NotBlank(message = "枪编号不能为空")
    @ApiModelProperty("枪编号")
    private String gunsNo;

    /**
     * 操作来源
     */
    @EnumValue
    @NotNull(message = "操作来源不能为空")
    @ApiModelProperty("操作来源 微信、阿里")
    private OrderSourceEnum optionType;

    /**
     * 操作来源
     */
    @ApiModelProperty("车牌号")
    private String plateNo;

    /**
     * 支付方式为空
     */
    @EnumValue
    @NotNull(message = "支付方式为空")
    @ApiModelProperty("支付方式")
    private OrderPayTypeEnum payType;

    /**
     * 预付费充值订单
     */
    @ApiModelProperty("预付费充值订单")
    private String rechargeOrderNo;

    public String getRechargeOrderNo() {
        if (rechargeOrderNo == null) {
            return StringUtils.EMPTY;
        }
        return rechargeOrderNo;
    }
}
