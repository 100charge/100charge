package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.domain.req.OrderSettlementRequest;
import com.xingchuan.charging.domain.req.StartChargingRequest;
import com.xingchuan.charging.domain.resp.ChargingRealtimeDataStatistics;
import com.xingchuan.charging.service.IChargingService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@Api(tags = "充电实况")
@RequestMapping("/charging")
public class ChargingController extends BaseController {

    @Resource
    private IChargingService chargingService;


    /**
     * 获取桩的实时状态
     */
    @GetMapping("/getRealTimeStatus")
    @ApiOperation(value = "获取桩的实时状态")
    public AjaxResult getRealTimeStatus(@ApiParam("场站id") @NotNull(message = "请选择场站") @RequestParam(value = "stationId") Long stationId) {
        ChargingRealtimeDataStatistics response = chargingService.getRealTimeStatus(stationId);
        return success(response);
    }

    /**
     * 开始充电、启动充电
     */
    @RepeatSubmit
    @GetMapping("/startCharging")
    @ApiOperation(value = "充电管理-开始充电")
    @Log(title = "开始充电", businessType = BusinessType.INSERT)
    public AjaxResult startCharging(@Validated StartChargingRequest request) {
        return AjaxResult.success("操作成功", chargingService.startCharging(request));
    }

    /**
     * 结束充电、停止充电
     */
    @RepeatSubmit
    @GetMapping("/endCharge")
    @ApiOperation(value = "充电管理-结束充电")
    @Log(title = "结束充电", businessType = BusinessType.INSERT)
    public AjaxResult endCharge(@RequestParam(value = "tradeNo", defaultValue = "") String tradeNo) {
        String errMsg = chargingService.endCharge(tradeNo);

        if (StringUtils.isBlank(errMsg)) {
            return AjaxResult.success();
        }
        return AjaxResult.error(errMsg);
    }

    /**
     * 充电订单支付
     */
    @RepeatSubmit(interval = 3000)
    @GetMapping("/orderPayment")
    @ApiOperation(value = "充电订单支付")
    @Log(title = "订单支付", businessType = BusinessType.INSERT)
    public AjaxResult orderPayment(@Validated OrderSettlementRequest request) {
        chargingService.orderPayment(request);
        return AjaxResult.success();
    }


}
