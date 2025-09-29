package com.xingchuan.charging.domain.resp;

import com.xingchuan.charging.domain.model.ChargingRealtimeData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "实时数据统计")
public class ChargingRealtimeDataStatistics {

    /**
     * 全部
     */
    @ApiModelProperty(value = "全部")
    private Integer total;

    /**
     * 空闲
     */
    @ApiModelProperty(value = "空闲")
    private Integer online;

    /**
     * 充电中
     */
    @ApiModelProperty(value = "充电中")
    private Integer charging;

    /**
     * 启动中
     */
    @ApiModelProperty(value = "启动中")
    private Integer starting;

    /**
     * 插枪
     */
    @ApiModelProperty(value = "插枪")
    private Integer gunInserted;

    /**
     * 离线
     */
    @ApiModelProperty(value = "离线")
    private Integer offline;

    /**
     * 故障
     */
    @ApiModelProperty(value = "故障")
    private Integer fault;

    /**
     * 已结束
     */
    @ApiModelProperty(value = "已结束")
    private Integer finished;

    /**
     * 响应列表
     */
    @ApiModelProperty(value = "响应列表")
    private List<ChargingRealtimeData> responseList;


    public Integer getTotal() {
        if (responseList == null) {
            return 0;
        }
        return total;
    }

    public Integer getOnline() {
        if (responseList == null) {
            return 0;
        }
        return online;
    }

    public Integer getCharging() {
        if (responseList == null) {
            return 0;
        }
        return charging;
    }

    public Integer getStarting() {
        if (responseList == null) {
            return 0;
        }
        return starting;
    }

    public Integer getGunInserted() {
        if (responseList == null) {
            return 0;
        }
        return gunInserted;
    }

    public Integer getOffline() {
        if (responseList == null) {
            return 0;
        }
        return offline;
    }

    public Integer getFault() {
        if (responseList == null) {
            return 0;
        }
        return fault;
    }

    public Integer getFinished() {
        if (responseList == null) {
            return 0;
        }
        return finished;
    }
}
