package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.domain.req.PayRequest;
import com.xingchuan.charging.domain.req.RefundRequest;
import com.xingchuan.charging.wechat.service.IPayService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付相关的接口
 */
@RestController
@Api(tags = "支付")
@RequestMapping("/pay")
public class PayController {

    private final IPayService payService;

    @Autowired
    public PayController(IPayService payService) {
        this.payService = payService;
    }

    /**
     * 用户充值
     *
     * @param payRequest 支付请求
     * @return 结果
     */
    @PostMapping("/recharge")
    @ApiOperation(value = "用户充值")
    @Log(title = "小程序-用户充值", businessType = BusinessType.INSERT)
    public AjaxResult aLiUserRecharge(@RequestBody PayRequest payRequest) {
        return AjaxResult.success(payService.recharge(payRequest));
    }

    /**
     * 用户提现
     *
     * @param refundRequest 退款请求
     * @return 结果
     */
    @RepeatSubmit(interval = 30000)
    @PostMapping("/withdrawal")
    @ApiOperation(value = "用户提现")
    @Log(title = "小程序-用户提现", businessType = BusinessType.INSERT)
    public AjaxResult userWithdrawal(@RequestBody RefundRequest refundRequest) {
        payService.refund(refundRequest);
        return AjaxResult.success();
    }
}
