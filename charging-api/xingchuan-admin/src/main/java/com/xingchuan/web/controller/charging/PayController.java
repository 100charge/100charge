package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.req.PayRequest;
import com.xingchuan.charging.domain.req.RefundRequest;
import com.xingchuan.charging.domain.resp.UserWithdrawalListResponse;
import com.xingchuan.charging.service.IUserWithdrawalRequestService;
import com.xingchuan.charging.wechat.service.IPayService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import com.xingchuan.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付相关的接口
 */
@RestController
@Api(tags = "支付")
@RequestMapping("/pay")
public class PayController extends BaseController {

    private final IPayService payService;
    private final IUserWithdrawalRequestService userWithdrawalRequestService;

    @Autowired
    public PayController(IPayService payService, 
            IUserWithdrawalRequestService userWithdrawalRequestService) {
        this.payService = payService;
        this.userWithdrawalRequestService = userWithdrawalRequestService;
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
    @GetMapping("/withdrawal")
    @ApiOperation(value = "用户提现")
    @Log(title = "小程序-用户提现", businessType = BusinessType.INSERT)
    public AjaxResult userWithdrawal(RefundRequest refundRequest) {
        payService.refund(refundRequest);
        return AjaxResult.success();
    }

    /**
     * 分页查询用户提现申请记录
     */
    @GetMapping("/getWithdrawalApplicationPage")
    @ApiOperation(value = "分页查询用户提现申请记录")
    public TableDataInfo queryByPage() {
        String userOpenId = SecurityUtils.getUserOpenId();
        Page<UserWithdrawalListResponse> response = userWithdrawalRequestService.queryByPageMini(userOpenId);
        return getDataTable(response);
    }
}
