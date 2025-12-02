package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.ChargingStationReviews;
import com.xingchuan.charging.domain.req.EvaluationRequest;
import com.xingchuan.charging.domain.resp.EvaluationListResponse;
import com.xingchuan.charging.domain.resp.UserEvaluationResponse;
import com.xingchuan.charging.domain.req.ChargingStationEvaluationPageRequest;
import com.xingchuan.charging.domain.resp.ChargingStationEvaluationPageResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 场站评价Service接口
 *
 * @author ruoyi
 */
public interface IChargingStationReviewsService extends IService<ChargingStationReviews> {

    /**
     * 订单评价
     */
    void orderEvaluation(EvaluationRequest request);

    /**
     * 查看场站评价
     */
    List<EvaluationListResponse> getEvaluation(Long stationId);

    /**
     * 根据订单查看评价
     */
    List<UserEvaluationResponse> getEvaluationByOrder(Long stationId, Long orderId);
    /**
     * 评价分页
     */
    Page<ChargingStationEvaluationPageResponse> getEvaluationPageList(ChargingStationEvaluationPageRequest request);
}
