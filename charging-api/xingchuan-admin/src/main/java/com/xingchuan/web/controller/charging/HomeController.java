package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.domain.req.HomeBusinessTrendRequest;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.service.IHomeService;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 首页-控制层Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/home")
@Api(tags = "首页-控制层")
public class HomeController extends BaseController {

    @Resource
    private IHomeService homeService;


    /**
     * 首页-查询今日、昨日数据
     */
    @GetMapping("/getStationDayCharging")
    @ApiOperation(value = "首页-查询今日、昨日数据")
    public AjaxResult getStationDayCharging(@RequestParam("stationId") Long stationId) {
        HomeDayChargingDataResponse response = homeService.getStationDayCharging(stationId);
        return success(response);
    }

    /**
     * 首页-获取场站实时状态数据
     */
    @GetMapping("/getStationRealtimeStatus")
    @ApiOperation(value = "首页-获取场站实时状态数据")
    public AjaxResult getStationRealtimeStatus(@RequestParam(value = "stationId", defaultValue = "-1") Long stationId) {
        HomeChargingPileStatusResponse response = homeService.getStationRealtimeStatusData(stationId);
        return success(response);
    }

    /**
     * 首页-获取场站经营趋势数据
     */
    @GetMapping("/getStationBusinessTrend")
    @ApiOperation(value = "首页-获取场站经营趋势数据")
    public AjaxResult getStationBusinessTrend(HomeBusinessTrendRequest request) {
        List<HomeBusinessTrendResponse> response = homeService.getStationBusinessTrend(request);
        return success(response);
    }

    /**
     * 首页-获取场站数据统计
     */
    @GetMapping("/getStationDataStatistics")
    @ApiOperation(value = "首页-获取场站数据统计")
    public AjaxResult getStationDataStatistics(@RequestParam("stationId") @NotNull(message = MessageConstants.CHARGING_STATIONS_NOT_EXIST) Long stationId) {
        StationDataStatisticsResponse response = homeService.getStationDataStatistics(stationId);
        return success(response);
    }

    /**
     * 首页-查询场站数据
     */
    @ApiOperation("首页-查询场站数据")
    @GetMapping("/getDashboardOperatorInfo")
    public AjaxResult getDashboardOperatorInfo() {
        DashboardOperatorInfoResponse response = homeService.getDashboardOperatorInfo();
        return success(response);
    }

    /**
     * 首页-场站收益数据
     */
    @ApiOperation("首页-场站收益数据")
    @GetMapping("/queryOperationData")
    public AjaxResult queryOperationData() {
        DashboardOperationDataResponse response = homeService.queryOperationData();
        return success(response);
    }


}
