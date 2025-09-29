package com.xingchuan.charging.domain.entity;

import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 场站附件 charging_station_annex
 *
 * @author ruoyi
 */
@Data
@NoArgsConstructor
public class ChargingStationAnnex extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @ApiModelProperty("用户ID")
    private Long id;

    /**
     * 场站id
     */
    @Excel(name = "场站id")
    @ApiModelProperty("场站id")
    private Long stationId;

    /**
     * 类型（1log  2场站图片  3营业执照）
     */
    @Excel(name = "类型（1log  2场站图片  3营业执照）")
    @ApiModelProperty("类型（1log  2场站图片  3营业执照）")
    private Integer type;

    /**
     * 文件地址
     */
    @Excel(name = "文件地址")
    @ApiModelProperty("文件地址")
    private String image;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    @ApiModelProperty("组织架构ID")
    private Long deptId;

    /**
     * '0'删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("'0'删除标志（0代表存在 2代表删除")
    private String delFlag;


    public ChargingStationAnnex(Long stationId, Integer type, String image, Long deptId) {
        this.stationId = stationId;
        this.type = type;
        this.image = image;
        this.deptId = deptId;
    }


}
