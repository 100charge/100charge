package com.xingchuan.charging.domain.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 充电桩对象 charging_pile
 */
@Data
public class ChargingPile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 场站id
     */
    @Excel(name = "场站id")
    private Long stationId;

    /**
     * 充电桩设备号
     */
    @Excel(name = "充电桩设备号")
    private String deviceNo;

    /**
     * 充电桩名称
     */
    @Excel(name = "充电桩名称")
    private String name;

    /**
     * 额定功率/输出功率范围-最高
     */
    @Excel(name = "额定功率/输出功率范围-最高")
    private Integer maxPower;

    /**
     * 额定功率/输出功率范围-最低
     */
    @Excel(name = "额定功率/输出功率范围-最低")
    private Integer minPower;

    /**
     * 额定电压/输出电压范围-最高
     */
    @Excel(name = "额定电压/输出电压范围-最高")
    private BigDecimal maxVoltage;

    /**
     * 额定电压/输出电压范围-最低
     */
    @Excel(name = "额定电压/输出电压范围-最低")
    private BigDecimal minVoltage;

    /**
     * 额定电流/输出电流范围-最高
     */
    @Excel(name = "额定电流/输出电流范围-最高")
    private BigDecimal maxCurrent;

    /**
     * 额定电流/输出电流范围-最低
     */
    @Excel(name = "额定电流/输出电流范围-最低")
    private BigDecimal minCurrent;

    /**
     * 充电桩类型 0:交流 1:直流
     */
    @Excel(name = "充电桩类型 0:交流,1:直流")
    private Integer pileType;

    /**
     * 设备状态  1:启用  0:禁用
     */
    @Excel(name = "设备状态  1:启用,0:禁用")
    private Integer pileStatus;

    /**
     * 生产厂商
     */
    @Excel(name = "生产厂商")
    private String supplier;

    /**
     * 硬件版本
     */
    @Excel(name = "硬件版本")
    private String hardwareVersion;

    /**
     * 协议版本
     */
    @Excel(name = "协议版本")
    private String apiVersion;

    /**
     * 协议版本
     */
    @Excel(name = "协议版本")
    private Integer protocolVersion;

    /**
     * 枪数量
     */
    @Excel(name = "枪数量")
    private Integer gunNumber;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    @TableField(fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 运营商id
     */
    @Excel(name = "运营商id")
    @TableField(fill = FieldFill.INSERT)
    private Long tenantId;

    /**
     * 互联互通-运营商id
     */
    private String operatorId;

    /**
     * 充电设备接口类型
     */
    @NotNull(message = "充电设备接口类型不能为空")
    private Integer connectorType;

    /**
     * 国家标准
     */
    @NotNull(message = "国家标准不能为空")
    private Integer nationalStandard;

}
