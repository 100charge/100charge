package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.wechat.service.IPayNotifyService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.enums.BusinessType;
import com.xingchuan.common.enums.OperatorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 支付回调接口
 */
@Slf4j
@RestController
@RequestMapping("/payNotify")
public class PayNotifyController extends BaseController {
    // 锁
    private final ReentrantLock lock = new ReentrantLock();

    private final IPayNotifyService payNotifyService;

    @Autowired
    public PayNotifyController(IPayNotifyService payNotifyService) {
        this.payNotifyService = payNotifyService;
    }

    /**
     * 微信充值订单结果通知
     */
    @PostMapping("/recharge/wechat")
    
    @Log(title = "微信充值订单结果通知", businessType = BusinessType.PAYMENT, operatorType = OperatorType.OTHER)
    public ResponseEntity<?> wechatRechargeNotify(HttpServletRequest request) throws IOException {
        lock.lock();
        try {
            log.debug("微信充值订单结果通知");
            String serial = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            return payNotifyService.handleWechatRechargeNotify(serial, signature, timestamp, nonce, body);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 微信退款/提现订单结果通知
     */
    @RepeatSubmit
    @PostMapping("/refund/wechat")
    @Log(title = "微信退款订单结果通知", businessType = BusinessType.PAYMENT, operatorType = OperatorType.OTHER)
    public ResponseEntity<?> wechatRefundNotify(HttpServletRequest request) throws IOException {
        lock.lock();
        try {
            log.debug("微信退款订单结果通知");
            String serial = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            return payNotifyService.handleWechatRefundNotify(serial, signature, timestamp, nonce, body);
        } finally {
            lock.unlock();
        }
    }

}
