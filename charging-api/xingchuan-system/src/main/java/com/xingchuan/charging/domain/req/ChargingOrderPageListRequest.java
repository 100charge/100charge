package com.xingchuan.charging.domain.req;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingchuan.charging.enums.AppUserEnum;
import com.xingchuan.charging.enums.OrderPayStatusEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 充电订单分页查询返回实体
 */
@Data
public class ChargingOrderPageListRequest {

    /**
     * 场站id
     */
    @ApiModelProperty("场站id")
    private Long stationId;

    /**
     * 订单号（唯一）
     */
    @ApiModelProperty("订单号（唯一）")
    private String tradeNo;

    /**
     * 三方订单号（唯一）
     */
    @ApiModelProperty("三方订单号（唯一）")
    private String outTradeNo;

    /**
     * 桩编号
     */
    @ApiModelProperty("桩编号")
    private String deviceNo;

    /**
     * 枪编号
     */
    @ApiModelProperty("枪编号")
    private String gunNo;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    private String plateNo;

    /**
     * 订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常
     */
    @ApiModelProperty("订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常")
    private Integer orderState;

    /**
     * 订单来源，订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin,5：互联互通,6：三方互联
     */
    @ApiModelProperty("订单来源，1：微信小程序,2：支付宝小程序,3：钥匙卡,4：vin,5：互联互通,6：三方互联")
    private Integer orderSource;

    /**
     * 是否离线订单（0离线订单；1在线订单）
     */
    @ApiModelProperty("是否离线订单（0离线订单；1在线订单）")
    private Boolean offline;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 订单来源平台
     */
    @ApiModelProperty("订单来源平台")
    private String platform;

    /**
     * 设备所属平台
     */
     @ApiModelProperty("设备所属平台")
    private String devicePlatform;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private Integer userType;

    /**
     * 支付状态
     */
    @EnumValue
     @ApiModelProperty("支付状态")
    private OrderPayStatusEnum payStatus;


    public List<Integer> getUserType() {
        List<Integer> list = new ArrayList<>();
        if (userType == null) {
            return list;
        }
        list.add(AppUserEnum.WECHAT.getCode());
        list.add(AppUserEnum.ALIPAY.getCode());
        return list;
    }

}
