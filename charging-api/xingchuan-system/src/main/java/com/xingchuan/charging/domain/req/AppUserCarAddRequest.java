package com.xingchuan.charging.domain.req;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.xingchuan.charging.enums.AppUserCarEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * app用户新增对象
 */
@Data
public class AppUserCarAddRequest {

    /**
     * 小程序用户车辆枚举类
     */
    @EnumValue
    @NotNull(message = "车辆用途不能为空")
    @ApiModelProperty(value = "车辆用途 PRIVATE_CAR:私家车、RIDE_HAILING_CAR:网约车、TAXI:出租车、LOGISTICS_VEHICLE:物流车")
    private AppUserCarEnum carType;

    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    @NotBlank(message = "车牌号不能为空")
    @Pattern(message = "车牌号有误或非新能源车牌", regexp = "^(京|津|沪|渝|冀|豫|云|辽|黑|湘|皖|鲁|新|苏|浙|赣|鄂|桂|甘|晋|蒙|陕|吉|闽|贵|粤|青|藏|川|宁|琼)[A-Z][A-Z0-9]{6}$")
    private String plateNo;

    /**
     * 是否默认车辆
     */
    @ApiModelProperty("是否默认车辆")
    private Boolean isDefault = Boolean.FALSE;
}
