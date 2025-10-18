package com.xingchuan.charging.service;


import com.xingchuan.charging.domain.req.HomeBusinessTrendRequest;
import com.xingchuan.charging.domain.resp.*;

import java.util.List;

/**
 * 首页Service接口
 *
 * @author ruoyi
 */
public interface IHomeService {

    /**
     * 首页-查询今日、昨日数据
     *
     * @param stationId 场站id
     * @return 结果
     */
    HomeDayChargingDataResponse getStationDayCharging(Long stationId);

    /**
     * 首页-获取场站实时状态数据
     *
     * @param stationId 场站id
     * @return 结果
     */
    HomeChargingPileStatusResponse getStationRealtimeStatusData(Long stationId);

    /**
     * 获取场站经营趋势数据
     *
     * @param request 请求参数
     * @return 结果
     */
    List<HomeBusinessTrendResponse> getStationBusinessTrend(HomeBusinessTrendRequest request);

    /**
     * 首页-获取场站数据统计
     */
    StationDataStatisticsResponse getStationDataStatistics(Long stationId);

    /**
     * 首页-查询场站数据
     */
    DashboardOperatorInfoResponse getDashboardOperatorInfo();

    /**
     * 首页-场站收益数据
     */
    DashboardOperationDataResponse queryOperationData();
}
