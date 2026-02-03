package com.xingchuan.charging.wechat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import com.xingchuan.charging.domain.entity.AppUserBalance;
import com.xingchuan.charging.domain.entity.AppUserBalanceRecord;
import com.xingchuan.charging.domain.entity.PayApiLog;
import com.xingchuan.charging.enums.AppUserBalanceRecordEnum;
import com.xingchuan.charging.enums.BalanceRecordStatusEnum;
import com.xingchuan.charging.enums.CallDirectionEnum;
import com.xingchuan.charging.enums.PayApiSourceEnum;
import com.xingchuan.charging.enums.PayState;
import com.xingchuan.charging.mapper.AppUserBalanceMapper;
import com.xingchuan.charging.mapper.AppUserBalanceRecordMapper;
import com.xingchuan.charging.wechat.config.WeChatPayConfig;
import com.xingchuan.charging.wechat.model.WechatNotifyRequest;
import com.xingchuan.charging.wechat.model.enums.WechatRequestMethod;
import com.xingchuan.charging.wechat.service.IPayLogService;
import com.xingchuan.charging.wechat.service.IPayNotifyService;
import com.xingchuan.charging.wechat.utils.AmountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PayNotifyServiceImpl implements IPayNotifyService {

    private final WeChatPayConfig config;
    private final IPayLogService logService;
    private final AppUserBalanceMapper appUserBalanceMapper;
    private final AppUserBalanceRecordMapper balanceRecordMapper;

    @Autowired
    public PayNotifyServiceImpl(WeChatPayConfig config, IPayLogService logService,
            AppUserBalanceMapper balanceMapper,
            AppUserBalanceRecordMapper balanceRecordMapper) {
        this.config = config;
        this.logService = logService;
        this.appUserBalanceMapper = balanceMapper;
        this.balanceRecordMapper = balanceRecordMapper;
    }

    /**
     * 处理微信充值回调
     *
     * @param serial    验签的微信支付平台证书序列号/微信支付公钥ID
     * @param signature 验签的签名值
     * @param timestamp 验签的时间戳
     * @param nonce     验签的随机字符串
     * @param body      请求主体中会包含JSON格式的通知参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> handleWechatRechargeNotify(String serial, String signature, String timestamp,
            String nonce, String body) {
        // 构建请求
        WechatNotifyRequest notifyRequest = buildWechatNotifyRequest(serial, signature, timestamp, nonce, body);
        Map<String, Object> errorMap = new HashMap<>();
        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(notifyRequest.getSerial())
                .nonce(notifyRequest.getNonce())
                .signature(notifyRequest.getSignature())
                .timestamp(notifyRequest.getTimestamp())
                .body(notifyRequest.getBody())
                .build();
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(config.getRSAPublicKeyConfig());
        Transaction response = parser.parse(requestParam, Transaction.class);

        try {
            createAndSaveEsPayApiLog(WechatRequestMethod.PAY_NOTIFY.getMethod(),
                    WechatRequestMethod.PAY_NOTIFY.getDescription(),
                    notifyRequest, response, true, errorMap);
            if (null == response) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (MalformedMessageException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            errorMap.put("error", "充值回调验签失败:" + e.getMessage());
            log.error("sign verification failed:{ex}", e);
            createAndSaveEsPayApiLog(WechatRequestMethod.PAY_NOTIFY.getMethod(),
                    WechatRequestMethod.PAY_NOTIFY.getDescription(),
                    notifyRequest, response, false, errorMap);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.debug("微信充值回调实体:{}", objectMapper.writeValueAsString(response));
            handlerRechargeNotify(response.getTransactionId(), response.getOutTradeNo(),
                    response.getAmount().getTotal(), buildPayStateByWechat(response.getTradeState()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 处理微信充值回调
     *
     * @param serial    验签的微信支付平台证书序列号/微信支付公钥ID
     * @param signature 验签的签名值
     * @param timestamp 验签的时间戳
     * @param nonce     验签的随机字符串
     * @param body      请求主体中会包含JSON格式的通知参数
     */
    @Override
    public ResponseEntity<?> handleWechatRefundNotify(String serial, String signature, String timestamp, String nonce,
            String body) {
        // 构建请求
        WechatNotifyRequest notifyRequest = buildWechatNotifyRequest(serial, signature, timestamp, nonce, body);
        Map<String, Object> errorMap = new HashMap<>();
        RefundNotification refundNotification = null;
        try {
            // 构造 RequestParam
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(notifyRequest.getSerial())
                    .nonce(notifyRequest.getNonce())
                    .signature(notifyRequest.getSignature())
                    .timestamp(notifyRequest.getTimestamp())
                    .body(notifyRequest.getBody())
                    .build();
            // 初始化 NotificationParser
            NotificationParser parser = new NotificationParser(config.getRSAPublicKeyConfig());
            refundNotification = parser.parse(requestParam, RefundNotification.class);

            createAndSaveEsPayApiLog(WechatRequestMethod.REFUNDS_NOTIFY.getMethod(),
                    WechatRequestMethod.REFUNDS_NOTIFY.getDescription(),
                    refundNotification, refundNotification, true, errorMap);

            if (null == refundNotification) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (MalformedMessageException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            errorMap.put("error", "退款回调验签失败:" + e.getMessage());
            log.error("sign verification failed:{}", e.getMessage(), e);
            createAndSaveEsPayApiLog(WechatRequestMethod.REFUNDS_NOTIFY.getMethod(),
                    WechatRequestMethod.REFUNDS_NOTIFY.getDescription(),
                    refundNotification, refundNotification, false, errorMap);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.debug("微信退款回调实体:{}", objectMapper.writeValueAsString(refundNotification));
            switch (refundNotification.getRefundStatus()) {
                case SUCCESS:
                    log.debug("退款成功");
                    handlerRefundSuccessNotification(refundNotification.getOutRefundNo());
                    break;
                case CLOSED:
                    log.debug("退款关闭,进行恢复余额或者预付费退款操作");
                    // 常规退款需要恢复余额
                    BigDecimal closedRefundAmount = AmountUtil.convertY2CNY(refundNotification.getAmount().getRefund());
                    handlerRefundErrorNotification(refundNotification.getOutTradeNo(),
                            refundNotification.getTransactionId(),
                            closedRefundAmount);
                    break;
                case ABNORMAL:
                    log.debug("退款异常,需要手动处理或者发起异常退款流程");
                    // 常规退款需要恢复余额
                    BigDecimal refundAmount = AmountUtil.convertY2CNY(refundNotification.getAmount().getRefund());
                    handlerRefundErrorNotification(refundNotification.getOutTradeNo(),
                            refundNotification.getTransactionId(),
                            refundAmount);
                    break;
                case PROCESSING:
                    log.debug("退款处理中");
                    break;
            }
        } catch (Exception e) {
            // 即使处理失败也返回200，避免微信重复回调
            log.error("处理微信退款回调异常，但仍返回成功以停止重试: {}", e.getMessage(), e);
        }
        // 返回200状态码，告诉微信已成功接收通知
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 构造微信回调请求
     *
     * @param serial    验签的微信支付平台证书序列号/微信支付公钥ID
     * @param signature 验签的签名值
     * @param timestamp 验签的时间戳
     * @param nonce     验签的随机字符串
     * @param body      请求主体中会包含JSON格式的通知参数
     */
    private WechatNotifyRequest buildWechatNotifyRequest(String serial, String signature,
            String timestamp, String nonce, String body) {
        WechatNotifyRequest notifyRequest = new WechatNotifyRequest();
        notifyRequest.setSerial(serial);
        notifyRequest.setSignature(signature);
        notifyRequest.setTimestamp(timestamp);
        notifyRequest.setNonce(nonce);
        notifyRequest.setBody(body);
        return notifyRequest;
    }

    /**
     * 检查处理支付回调
     *
     * @param outTradeNo  三方订单号
     * @param tradeNo     商户订单号
     * @param totalAmount 总金额
     * @param payState    支付状态
     */
    private void handlerRechargeNotify(String outTradeNo, String tradeNo, Integer totalAmount, PayState payState) {
        AppUserBalanceRecord record = null;
        try {
            record = balanceRecordMapper.selectOne(Wrappers.<AppUserBalanceRecord>lambdaQuery()
                    .eq(AppUserBalanceRecord::getTradeNo, tradeNo)
                    .eq(AppUserBalanceRecord::getType, AppUserBalanceRecordEnum.RECHARGE.getCode()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (null == record) {
            String remark = "微信支付回调,系统充值记录不存在";
            log.error(remark);
            return;
        }

        if (record.getStatus() == BalanceRecordStatusEnum.SUCCESS.getCode()) {
            String remark = "微信支付回调,系统充值记录已处理成功,无需重复处理";
            log.error(remark);
            return;
        }

        BigDecimal rechargeAmount = AmountUtil.convertY2CNY(totalAmount.longValue());
        record.setOutTradeNo(outTradeNo);
        if (rechargeAmount.compareTo(record.getAmount()) != 0) {
            String err = String.format("微信支付回调,系统充值记录金额:[%b]与实际不一致:[%b]", record.getAmount(), rechargeAmount);
            log.error(err);
            record.setStatus(BalanceRecordStatusEnum.FAILURE.getCode());
            record.setRemark(err);
            balanceRecordMapper.updateById(record);
            return;
        }
        switch (payState) {
            case SUCCESS:
                record.setStatus(BalanceRecordStatusEnum.SUCCESS.getCode());
                record.setLastAmount(rechargeAmount);
                record.setRefundableAmount(rechargeAmount);
                record.setRemainingAmount(rechargeAmount);
                balanceRecordMapper.updateById(record);
                insertUserBalance(rechargeAmount, record.getOpenId());
                break;
            case ERROR:
            case REFUND:
            case NOT_PAY:
            case CLOSED:
                record.setStatus(BalanceRecordStatusEnum.FAILURE.getCode());
                balanceRecordMapper.updateById(record);
                break;
        }
    }

    /**
     * 插入用户余额
     *
     * @param amount 充值金额
     * @param openId openId
     */
    private void insertUserBalance(BigDecimal amount, String openId) {
        // 增加余额
        AppUserBalance userBalance = appUserBalanceMapper.selectOne(Wrappers.<AppUserBalance>lambdaQuery()
                .select(AppUserBalance::getId, AppUserBalance::getBalance).eq(AppUserBalance::getOpenId, openId));
        if (ObjectUtils.isEmpty(userBalance)) {
            log.error("用户余额记录不存在,执行插入");
            userBalance = buildAppUserBalance(amount, openId);
            appUserBalanceMapper.insert(userBalance);
        } else {
            BigDecimal balance = userBalance.getBalance().add(amount);
            appUserBalanceMapper.update(new AppUserBalance(), Wrappers.<AppUserBalance>lambdaUpdate()
                    .set(AppUserBalance::getBalance, balance).eq(AppUserBalance::getId, userBalance.getId()));
        }
    }

    /**
     * 处理退款成功通知
     *
     * @param tradeNo    商户订单号
     * @param outTradeNo 三方订单号
     * @param amount     退款金额
     */
    private void handlerRefundSuccessNotification(String tradeNo) {
        AppUserBalanceRecord record = balanceRecordMapper.selectOne(Wrappers.<AppUserBalanceRecord>lambdaQuery()
                .eq(AppUserBalanceRecord::getTradeNo, tradeNo));
        if (record != null) {
            if (record.getStatus() == BalanceRecordStatusEnum.SUCCESS.getCode()) {
                log.error("用户退款订单已处理成功,无需重复处理,订单号:{}", tradeNo);
                return;
            }

            if (record.getStatus() == BalanceRecordStatusEnum.PROCESSING.getCode()) {
                record.setStatus(BalanceRecordStatusEnum.SUCCESS.getCode());
                //设置当前时间
                record.setUpdateTime(new Date());
                balanceRecordMapper.updateById(record);
            }
        } else {
            log.error("用户退款订单不存在,无法恢复金额,订单号:{}}", tradeNo);
        }
    }

    /**
     * 处理退款失败通知
     *
     * @param tradeNo    商户订单号
     * @param outTradeNo 三方订单号
     * @param amount     退款金额
     */
    private void handlerRefundErrorNotification(String tradeNo, String outTradeNo, BigDecimal amount) {
        AppUserBalanceRecord record = balanceRecordMapper.selectOne(Wrappers.<AppUserBalanceRecord>lambdaQuery()
                .select(AppUserBalanceRecord::getOpenId, AppUserBalanceRecord::getAmount,
                        AppUserBalanceRecord::getPayTradeNo)
                .eq(AppUserBalanceRecord::getTradeNo, tradeNo)
                .eq(AppUserBalanceRecord::getOutTradeNo, outTradeNo));
        if (record != null) {
            // 找到对应的充值订单
            AppUserBalanceRecord payRecord = balanceRecordMapper.selectOne(
                    Wrappers.<AppUserBalanceRecord>lambdaQuery()
                            .eq(AppUserBalanceRecord::getOpenId, record.getOpenId())
                            .eq(AppUserBalanceRecord::getTradeNo, record.getPayTradeNo()));
            if (ObjectUtils.isEmpty(payRecord)) {
                log.error("找不到用户openId:{}对应的充值订单:{},无法恢复可退款金额:{}", record.getOpenId(), record.getPayTradeNo(), amount);
            } else {
                payRecord.setRefundableAmount(payRecord.getRefundableAmount().add(amount));
                balanceRecordMapper.updateById(payRecord);
            }
            appUserBalanceMapper.updateBalanceByOpenId(record.getOpenId(), amount);
            record.setStatus(BalanceRecordStatusEnum.FAILURE.getCode());
            balanceRecordMapper.updateById(record);
        } else {
            log.error("用户退款订单不存在,无法恢复金额,订单号:{},三方订单号:{}", tradeNo, outTradeNo);
        }
    }

    /**
     * 构建用户余额
     *
     * @param balance 余额
     * @param openId  用户openid
     */
    private AppUserBalance buildAppUserBalance(BigDecimal balance, String openId) {
        AppUserBalance appUserBalance = new AppUserBalance();
        appUserBalance.setOpenId(openId);
        appUserBalance.setBalance(balance);
        return appUserBalance;
    }

    /**
     * 创建并且保存支付日志
     *
     * @param address  请求地址
     * @param desc     描述
     * @param request  请求参数
     * @param response 返回参数
     * @param success  是否成功
     * @param errMap   错误信息Map
     */
    private void createAndSaveEsPayApiLog(String address, String desc, Object request,
            Object response, boolean success,
            Map<String, Object> errMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestStr = objectMapper.writeValueAsString(request);
            String responseStr = objectMapper.writeValueAsString(response);
            String errString = objectMapper.writeValueAsString(errMap);
            PayApiLog apiLog = new PayApiLog();
            apiLog.setApiAddress(address);
            apiLog.setDesc(desc);
            apiLog.setRequest(requestStr);
            apiLog.setResponse(responseStr);
            apiLog.setSuccess(success);
            apiLog.setCallDirection(CallDirectionEnum.INCOMING.getName());
            apiLog.setPayApiSource(PayApiSourceEnum.WECHAT_PAY.getName());
            apiLog.setErrorMsg(errString);
            apiLog.setCreateTime(LocalDateTime.now());
            logService.saveLogs(apiLog);
        } catch (Exception e) {
            log.error("保存微信支付相关日志失败:{}", e.getMessage(), e);
        }
    }

    /**
     * 构建微信支付状态
     */
    private PayState buildPayStateByWechat(Transaction.TradeStateEnum stateEnum) {
        switch (stateEnum) {
            case SUCCESS:
                return PayState.SUCCESS;
            case REFUND:
                return PayState.REFUND;
            case NOTPAY:
                return PayState.NOT_PAY;
            case CLOSED:
                return PayState.CLOSED;
        }
        return PayState.ERROR;
    }

}
