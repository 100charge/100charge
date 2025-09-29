package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.excel.ChargingOrderExport;
import com.xingchuan.charging.domain.req.ChargingOrderPageListRequest;
import com.xingchuan.charging.domain.resp.*;
import com.xingchuan.charging.service.IChargingOrderService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import com.xingchuan.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/chargingOrder")
@Api(tags = "充电订单")
public class ChargingOrderController extends BaseController {

    @Resource
    private IChargingOrderService service;

    /**
     * 充电订单列表分页查询
     */
    @GetMapping("/list")
    @ApiOperation(value = "充电订单列表分页查询")
    public TableDataInfo getChargingOrder(ChargingOrderPageListRequest request) {
        Page<ChargingOrderPageListResponse> responsePage = service.selectPageList(request);
        return getDataTable(responsePage);
    }

    /**
     * 充电订单统计
     */
    @GetMapping(value = "/orderStatistics")
    @ApiOperation(value = "充电订单统计")
    public AjaxResult orderStatistics(ChargingOrderPageListRequest request) {
        OrderStatisticsResponse response = service.orderStatistics(request);
        return success(response);
    }

    /**
     * 充电订单详情查询
     */
    @GetMapping(value = "/orderDetail")
    @ApiOperation(value = "充电订单详情查询")
    public AjaxResult orderDetail(@ApiParam("订单id") @NotNull(message = "id不能为空!") @RequestParam("id") Long id) {
        ChargingOrderDetailResponse response = service.orderDetail(id);
        return success(response);
    }

    /**
     * 充电订单列表导出
     */
    @RepeatSubmit
    @PostMapping("/export")
    @ApiOperation(value = "充电订单列表导出")
    public void export(HttpServletResponse response, ChargingOrderPageListRequest request) {
        List<ChargingOrderExport> responseList = service.export(request);
        ExcelUtil<ChargingOrderExport> util = new ExcelUtil<>(ChargingOrderExport.class);
        util.exportExcel(response, responseList, "充电订单列表");
    }

    /**
     * 未支付订单自动支付
     */
    @GetMapping("/autoPayOrder")
    @ApiOperation(value = "未支付订单自动支付")
    @Log(title = "自动支付", businessType = BusinessType.OTHER)
    public AjaxResult autoPayOrder() {
        service.autoPayOrder();
        return success();
    }

    /**
     * 置为异常订单
     */
    @GetMapping("/markOrderAsAbnormal")
    @Log(title = "置为异常订单", businessType = BusinessType.UPDATE)
    public AjaxResult markOrderAsAbnormal(@RequestParam(value = "tradeNo") String tradeNo) {
        service.markOrderAsAbnormal(tradeNo);
        return success();
    }

    /**
     * 小程序-充电订单列表分页查询
     */
    @GetMapping("/miniOrderList")
    @ApiOperation(value = "小程序-充电订单列表分页查询")
    public TableDataInfo miniOrderList(@ApiParam("订单支付状态") @RequestParam(value = "status", required = false) Integer status) {
        Page<OrderPageListResponse> responsePage = service.miniOrderList(status);
        return getDataTable(responsePage);
    }

    /**
     * 充电订单详情查询
     */
    @GetMapping("/miniOrderDetail")
    @ApiOperation(value = "充电订单详情查询")
    public AjaxResult miniOrderDetail(@ApiParam("订单编号") @NotBlank(message = "订单编号必填") @RequestParam(value = "tradeNo") String tradeNo) {
        OrderDetailResponse response = service.miniOrderDetail(tradeNo);
        return success(response);
    }

    /**
     * 查询用户待支付订单
     */
    @GetMapping("/getPendingPaymentOrder")
    @ApiOperation(value = "查询用户待支付订单")
    public AjaxResult getPendingPaymentOrder() {
        PendingPaymentOrderResponse response = service.getPendingPaymentOrder();
        return success(response);
    }

    /**
     * 根据订单号查询订单状态
     */
    @GetMapping("/getOrderInfoByTradeNo")
    @ApiOperation(value = "根据订单号查询订单状态")
    public AjaxResult getOrderInfoByTradeNo(@RequestParam(value = "tradeNo", defaultValue = "") String tradeNo) {
        return success(service.getOrderInfoByTradeNo(tradeNo));
    }

}
