package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.domain.req.EvaluationRequest;
import com.xingchuan.charging.domain.resp.ChargingStationEvaluationPageResponse;
import com.xingchuan.charging.domain.resp.EvaluationListResponse;
import com.xingchuan.charging.domain.resp.UserEvaluationResponse;
import com.xingchuan.charging.service.IChargingStationReviewsService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.req.ChargingStationEvaluationPageRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/stationEvaluation")
@Api(tags = "评价管理")
public class StationEvaluationController extends BaseController {

    @Autowired
    private IChargingStationReviewsService reviewsService;

    /**
     * 订单评价
     */
    @PostMapping("/evaluation")
    @ApiOperation(value = "订单评价")
    @Log(title = "充电订单评价", businessType = BusinessType.INSERT)
    public AjaxResult orderEvaluation(@Validated @RequestBody EvaluationRequest request) {
        reviewsService.orderEvaluation(request);
        return success();
    }

    /**
     * 查看场站评价
     */
    @GetMapping("/getEvaluation")
    @ApiOperation(value = "查看场站评价")
    public AjaxResult getEvaluation(@ApiParam("场站id") @NotNull(message = "场站id不能为空!") @RequestParam("stationId") Long stationId) {
        List<EvaluationListResponse> response = reviewsService.getEvaluation(stationId);
        return success(response);
    }

    /**
     * 根据订单查看评价
     */
    @GetMapping("/getEvaluationByOrder")
    @ApiOperation(value = "根据订单查看评价")
    public AjaxResult getEvaluationByOrder(@ApiParam("场站id") @NotNull(message = "场站id不能为空!") @RequestParam("stationId") Long stationId,
                                           @ApiParam("订单id") @NotNull(message = "订单id不能为空!") @RequestParam("orderId") Long orderId) {
        List<UserEvaluationResponse> response = reviewsService.getEvaluationByOrder(stationId, orderId);
        return success(response);
    }

    @GetMapping("/list")
    @ApiOperation("评价分页")
    public TableDataInfo list(ChargingStationEvaluationPageRequest request) {
         Page<ChargingStationEvaluationPageResponse> page = reviewsService.getEvaluationPageList(request);
         return getDataTable(page);
    }
}
