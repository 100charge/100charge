package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 充电桩对象 charging_pile
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "充电桩新增请求实体类")
public class ChargingPileAddRequest {

    /**
     * 场站id
     */
    @NotNull(message = "场站id不能为空!")
    @ApiModelProperty(value = "场站id")
    private Long stationId;

    /**
     * 充电桩设备号
     */
    @Length(min = 14, max = 32, message = "桩编号格式异常")
    @NotBlank(message = "充电桩设备号不能为空!")
    @ApiModelProperty(value = "充电桩设备号")
    private String deviceNo;

    /**
     * 充电桩名称
     */
    @NotBlank(message = "充电桩名称不能为空!")
    @ApiModelProperty(value = "充电桩名称")
    private String name;

    /**
     * 额定功率/输出功率范围-最高
     */
    @NotNull(message = "最高输出功率不能为空!")
    @ApiModelProperty(value = "额定功率/输出功率范围-最高")
    @DecimalMin(value = "0", message = "最高输出功率不能小于0")
    @DecimalMax(value = "360", message = "最高输出功率不能大于360")
    private Integer maxPower;

    /**
     * 额定功率/输出功率范围-最低
     */
    @NotNull(message = "最低输出功率不能为空!")
    @ApiModelProperty(value = "额定功率/输出功率范围-最低")
    @DecimalMin(value = "0", message = "最低输出功率不能小于0")
    @DecimalMax(value = "360", message = "最低输出功率不能大于360")
    private Integer minPower;

    /**
     * 额定电压/输出电压范围-最高
     */
    @NotNull(message = "最高输出电压不能为空!")
    @ApiModelProperty(value = "额定电压/输出电压范围-最高")
    @DecimalMin(value = "50", message = "最高输出电不能小于50")
    @DecimalMax(value = "1000", message = "最高电压不能大于1000")
    private BigDecimal maxVoltage;

    /**
     * 额定电压/输出电压范围-最低
     */
    @NotNull(message = "最低输出电压不能为空!")
    @ApiModelProperty(value = "额定电压/输出电压范围-最低")
    @DecimalMin(value = "50", message = "最低输出电不能小于50")
    @DecimalMax(value = "1000", message = "最低输出电不能大于1000")
    private BigDecimal minVoltage;

    /**
     * 额定电流/输出电流范围-最高
     */
    @NotNull(message = "最高输出电流不能为空!")
    @ApiModelProperty(value = "额定电流/输出电流范围-最高")
    @DecimalMin(value = "0", message = "最高输出电流不能小于0")
    @DecimalMax(value = "900", message = "最高输出电流不能大于900")
    private BigDecimal maxCurrent;

    /**
     * 额定电流/输出电流范围-最低
     */
    @NotNull(message = "最低输出电流不能为空!")
    @ApiModelProperty(value = "额定电流/输出电流范围-最低")
    @DecimalMin(value = "0", message = "最低输出电流不能小于0")
    @DecimalMax(value = "900", message = "最低输出电流不能大于900")
    private BigDecimal minCurrent;

    /**
     * 充电桩类型 0:交流 1:直流
     */
    @NotNull(message = "充电桩类型不能为空!")
    @ApiModelProperty(value = "充电桩类型 0:交流,1:直流")
    private Integer pileType;

    /**
     * 枪数量
     */
    @NotNull(message = "枪数量不能为空!")
    @ApiModelProperty(value = "枪数量")
    @Min(value = 1, message = "充电桩至少要有一个枪")
    private Integer gunNumber;

    /**
     * 生产厂商
     */
    @NotBlank(message = "生产厂商不能为空!")
    @ApiModelProperty(value = "生产厂商")
    private String supplier;

    /**
     * 硬件版本
     */
    @NotBlank(message = "硬件版本不能为空!")
    @ApiModelProperty(value = "硬件版本")
    private String hardwareVersion;

    /**
     * 协议版本
     */
    @NotNull(message = "协议版本不能为空!")
    @ApiModelProperty(value = "协议版本")
    private Integer protocolVersion;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    /**
     * 充电设备接口类型
     */
    @NotNull(message = "充电设备接口类型不能为空")
    @ApiModelProperty(value = "充电设备接口类型")
    private Integer connectorType;

    /**
     * 国家标准
     */
    @NotNull(message = "国家标准不能为空")
    @ApiModelProperty(value = "国家标准")
    private Integer nationalStandard;
}
