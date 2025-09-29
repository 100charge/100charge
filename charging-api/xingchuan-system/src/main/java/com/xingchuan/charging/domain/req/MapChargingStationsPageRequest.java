package com.xingchuan.charging.domain.req;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.xingchuan.charging.enums.StationChargingSpeedEnum;
import com.xingchuan.charging.enums.StationSearchEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 地图场站分页对象
 *
 * @author ruoyi
 */
@Data
public class MapChargingStationsPageRequest {

    /**
     * 场站名
     */
    @ApiModelProperty("场站名")
    private String chargingStationName;

    /**
     * 城市行政编码
     */
    @NotBlank(message = "城市行政编码不能为空")
    @ApiModelProperty("城市行政编码")
    private String cityCode;

    /**
     * 推荐类型（智能、距离、价格）
     */
    @EnumValue
    @NotNull(message = "推荐类型不能为空")
    @ApiModelProperty("推荐类型（智能、距离、价格）")
    private StationSearchEnum recommendType = StationSearchEnum.AI;

    /**
     * 当前地图纬度
     */
    @NotNull(message = "当前地图纬度不能为空")
    @ApiModelProperty("当前纬度")
    private BigDecimal mapLat;

    /**
     * 当前地图经度
     */
    @NotNull(message = "当前地图经度不能为空")
    @ApiModelProperty("当前经度")
    private BigDecimal mapLng;

    /**
     * 当前纬度
     */
    @NotNull(message = "当前纬度不能为空")
    @ApiModelProperty("当前纬度")
    private BigDecimal lat;

    /**
     * 当前经度
     */
    @NotNull(message = "当前经度不能为空")
    @ApiModelProperty("当前经度")
    private BigDecimal lng;

    /**
     * 充电速度（>=180超充；30-179快充；<30慢充）
     */
    @EnumValue
    @ApiModelProperty("充电速度（>=180超充；30-179快充；<30慢充）")
    private StationChargingSpeedEnum chargingSpeed;

    /**
     * 场站服务（卫生间、休息室、便利店）
     */
    @ApiModelProperty("场站服务（卫生间、休息室、便利店）")
    private List<String> serviceLabel;

    /**
     * 设施标签（地上、地下、洗车）
     */
    @ApiModelProperty("设施标签（地上、地下、洗车）")
    private List<String> facilityLabel;

    /**
     * 停车费标签
     */
    @ApiModelProperty("停车费标签")
    private List<String> parkingFeeLabel;

}
