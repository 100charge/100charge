package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页-充电桩试试状态 返回对象
 */
@Data
@NoArgsConstructor
@ApiModel("首页-今日、昨日数据返回对象")
public class HomeChargingPileStatusResponse {

    /**
     * 桩数量
     */
    @ApiModelProperty("桩数量")
    private Integer pileCount = 0;
    /**
     * 离线桩数量
     */
    @ApiModelProperty("离线桩数量")
    private Integer offlinePileCount = 0;
    /**
     * 故障桩数量
     */
    @ApiModelProperty("故障桩数量")
    private Integer faultyPileCount = 0;
    /**
     * 空闲桩数量
     */
    @ApiModelProperty("空闲桩数量")
    private Integer idlePileCount = 0;
    /**
     * 启动中桩数量
     */
    @ApiModelProperty("启动中桩数量")
    private Integer startingPileCount = 0;
    /**
     * 充电中桩数量
     */
    @ApiModelProperty("充电中桩数量")
    private Integer chargingPileCount = 0;


    public HomeChargingPileStatusResponse(Integer pileCount, Integer offlinePileCount, Integer faultyPileCount,
                                          Integer idlePileCount, Integer startingPileCount, Integer chargingPileCount) {
        this.pileCount = pileCount;
        this.offlinePileCount = offlinePileCount;
        this.faultyPileCount = faultyPileCount;
        this.idlePileCount = idlePileCount;
        this.startingPileCount = startingPileCount;
        this.chargingPileCount = chargingPileCount;
    }
}
