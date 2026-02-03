package com.xingchuan.charging.wechat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import com.xingchuan.charging.domain.entity.AppUserBalance;
import com.xingchuan.charging.domain.entity.AppUserBalanceRecord;
import com.xingchuan.charging.domain.entity.PayApiLog;
import com.xingchuan.charging.domain.req.PayRequest;
import com.xingchuan.charging.domain.req.RefundRequest;
import com.xingchuan.charging.domain.req.UnifiedPayRequest;
import com.xingchuan.charging.domain.req.UnifiedRefundRequest;
import com.xingchuan.charging.domain.resp.BaseRefundResponse;
import com.xingchuan.charging.enums.*;
import com.xingchuan.charging.mapper.AppUserBalanceMapper;
import com.xingchuan.charging.mapper.AppUserBalanceRecordMapper;
import com.xingchuan.charging.service.IChargingOrderService;
import com.xingchuan.charging.wechat.config.WeChatPayConfig;
import com.xingchuan.charging.wechat.model.enums.WechatRequestMethod;
import com.xingchuan.charging.wechat.model.pay.WechatPayResponse;
import com.xingchuan.charging.wechat.model.refund.WechatPayRefundResponse;
import com.xingchuan.charging.wechat.service.IPayLogService;
import com.xingchuan.charging.wechat.service.IPayService;
import com.xingchuan.charging.wechat.utils.AmountUtil;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.exception.base.BaseException;
import com.xingchuan.common.utils.OrderNoUtils;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.system.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PayServiceImpl implements IPayService {
    /**
     * 充值金额最小值 字典
     */
    private static final String MIN_RECHARGE_AMOUNT = "system_minimum_recharge_amount";
    /**
     * 充值金额最大值 字典
     */
    private static final String MAX_RECHARGE_AMOUNT = "system_max_recharge_amount";

    private final WeChatPayConfig config;
    private final IPayLogService logService;
    private final IChargingOrderService orderService;
    private final ISysConfigService sysConfigService;
    private final AppUserBalanceMapper userBalanceMapper;
    private final AppUserBalanceRecordMapper userBalanceRecordMapper;

    public PayServiceImpl(ISysConfigService sysConfigService, AppUserBalanceMapper userBalanceMapper,
            AppUserBalanceRecordMapper userBalanceRecordMapper, WeChatPayConfig config, IPayLogService logService,
            IChargingOrderService orderService) {
        this.sysConfigService = sysConfigService;
        this.userBalanceMapper = userBalanceMapper;
        this.userBalanceRecordMapper = userBalanceRecordMapper;
        this.config = config;
        this.logService = logService;
        this.orderService = orderService;
    }

    /**
     * 获取微信的错误信息
     *
     * @param e      异常
     * @param source 来源
     */
    private static String getWechatErrorString(Exception e, String source) {
        String error = "";
        if (e instanceof MalformedMessageException) {
            error = MessageFormat.format("{0}异常：{1}", source, e.getMessage());
            log.error(error, e);
        } else if (e instanceof HttpException) {
            HttpException he = (HttpException) e;
            error = MessageFormat.format("{0}异常：{1}", source, he.getMessage());
        } else if (e instanceof com.wechat.pay.java.core.exception.ServiceException) {
            com.wechat.pay.java.core.exception.ServiceException se = (com.wechat.pay.java.core.exception.ServiceException) e;
            error = MessageFormat.format("{0}异常：{1}", source, se.getErrorMessage());
        }
        return error;
    }

    @Override
    public WechatPayResponse recharge(PayRequest payRequest) {
        // 充值前检查
        checkBeforeRecharge(payRequest.getAmount());
        // 生成充值订单编号
        String openId = SecurityUtils.getUserOpenId();
        String tradeNo = OrderNoUtils.generateRechargeOrderNo();
        // 插入充值订单
        createRechargeRecord(openId, payRequest.getAmount(), tradeNo, payRequest.getPayment());
        // 组装充值参数
        UnifiedPayRequest request = new UnifiedPayRequest();
        request.setHandlingFee(BigDecimal.ZERO);
        request.setOpenId(openId);
        request.setOutTradeNo(tradeNo);
        request.setAmount(payRequest.getAmount());
        request.setPayment(payRequest.getPayment());
        request.setPaySource(payRequest.getPaySource());
        request.setDescription("充值");

        Map<String, Object> errorMap = new HashMap<>();
        PrepayWithRequestPaymentResponse response = null;
        try {
            JsapiServiceExtension jsapiService = createJsapiService();

            PrepayRequest prepayRequest = createPrepayRequest(request);

            response = jsapiService.prepayWithRequestPayment(prepayRequest);
            WechatPayResponse payResponse = new WechatPayResponse();
            payResponse.setTimeStamp(response.getTimeStamp());
            payResponse.setNonceStr(response.getNonceStr());
            payResponse.setPackageVal(response.getPackageVal());
            payResponse.setPaySign(response.getPaySign());

            createAndSaveEsPayApiLog(WechatRequestMethod.JSAPI_PREPAY.getMethod(),
                    WechatRequestMethod.JSAPI_PREPAY.getDescription(),
                    prepayRequest, response, true, CallDirectionEnum.OUTGOING, errorMap);

            return payResponse;
        } catch (Exception e) {
            String error = getWechatErrorString(e, "预支付");
            errorMap.put("error", error);
            log.error(error, e);
            createAndSaveEsPayApiLog(WechatRequestMethod.JSAPI_PREPAY.getMethod(),
                    WechatRequestMethod.JSAPI_PREPAY.getDescription(),
                    payRequest, response, false, CallDirectionEnum.INCOMING, errorMap);
            throw new RuntimeException(error, e);
        }
    }

    @Override
    public void refund(RefundRequest request) {

        String openId = SecurityUtils.getUserOpenId();
        BigDecimal userBalance = checkBeforeRefund(openId, request.getAmount());
        // 获取用户可退款充值订单
        List<AppUserBalanceRecord> rechargeRecords = userBalanceRecordMapper.selectList(
                Wrappers.<AppUserBalanceRecord>lambdaQuery()
                        .eq(AppUserBalanceRecord::getOpenId, openId)
                        .eq(AppUserBalanceRecord::getStatus, BalanceRecordStatusEnum.SUCCESS.getCode())
                        .eq(AppUserBalanceRecord::getType, AppUserBalanceRecordEnum.RECHARGE.getCode())
                        .gt(AppUserBalanceRecord::getRefundableAmount, BigDecimal.ZERO)
                        .gt(AppUserBalanceRecord::getRemainingAmount, BigDecimal.ZERO)
                        .orderByAsc(AppUserBalanceRecord::getRefundableAmount));
        List<UnifiedRefundRequest> refundRequests = createUnifiedRefundRequest(request, rechargeRecords, openId);
        // 暂扣金额，审核失败时需要恢复
        userBalanceMapper.update(Wrappers.<AppUserBalance>lambdaUpdate()
                .set(AppUserBalance::getBalance, userBalance.subtract(request.getAmount()))
                .eq(AppUserBalance::getOpenId, openId));
        for (UnifiedRefundRequest refundRequest : refundRequests) {

            Map<String, Object> errorMap = new HashMap<>();
            Refund response = null;
            try {
                RefundService refundService = createRefundService();
                CreateRequest createRequest = createRefundRequest(refundRequest);
                response = refundService.create(createRequest);

                WechatPayRefundResponse wechatPayRefundResponse = new WechatPayRefundResponse();
                wechatPayRefundResponse.setTradeNo(response.getOutRefundNo());
                wechatPayRefundResponse.setOutRefundNo(response.getRefundId());

                createRefundRecord(openId, refundRequest.getAmount(), refundRequest.getRefundNo(),
                    wechatPayRefundResponse.getOutRefundNo(), refundRequest.getOutRequestNo(), refundRequest.getTradeNo(), refundRequest.getPayment());

                createAndSaveEsPayApiLog(WechatRequestMethod.REFUNDS.getMethod(),
                        WechatRequestMethod.REFUNDS.getDescription(),
                        createRequest,
                        response,
                        true, CallDirectionEnum.OUTGOING, null);
            } catch (Exception e) {
                String error = getWechatErrorString(e, "退款");
                errorMap.put("error", error);
                createAndSaveEsPayApiLog(WechatRequestMethod.REFUNDS_NOTIFY.getMethod(),
                        WechatRequestMethod.REFUNDS_NOTIFY.getDescription(),
                        request, response, false, CallDirectionEnum.INCOMING, errorMap);
                throw new RuntimeException(error, e);
            }
        }
    }

    /**
     * 充值前检查
     *
     * @param amount 充值金额
     */
    private void checkBeforeRecharge(BigDecimal amount) {
        // 查询并判断最低充值金额
        String minimumRechargeAmountStr = sysConfigService.selectConfigByKey(MIN_RECHARGE_AMOUNT);
        BigDecimal minimumRechargeAmount = StringUtils.isEmpty(minimumRechargeAmountStr) ? BigDecimal.ZERO
                : new BigDecimal(minimumRechargeAmountStr);
        if (amount.compareTo(minimumRechargeAmount) < 0) {
            throw new ServiceException(MessageConstants.RECHARGE_AMOUNT_LESS_THAN_MINIMUM + minimumRechargeAmount);
        }
        // 查询并判断最大充值金额
        String maxRechargeAmountStr = sysConfigService.selectConfigByKey(MAX_RECHARGE_AMOUNT);
        BigDecimal maxRechargeAmount = StringUtils.isEmpty(maxRechargeAmountStr) ? BigDecimal.ZERO
                : new BigDecimal(maxRechargeAmountStr);
        if (amount.compareTo(maxRechargeAmount) > 0) {
            throw new ServiceException(MessageConstants.RECHARGE_AMOUNT_MORE_THAN_MAXIMUM + maxRechargeAmount);
        }
    }

    /**
     * 创建充值订单
     *
     * @param amount  充值金额
     * @param openId  用户的openid
     * @param tradeNo 订单号
     * @param payment 支付方式
     */
    private void createRechargeRecord(String openId, BigDecimal amount, String tradeNo, Payment payment) {
        // 获取用户可用余额
        AppUserBalance appUserBalance = userBalanceMapper.selectOne(Wrappers.<AppUserBalance>lambdaQuery()
                .select(AppUserBalance::getId, AppUserBalance::getBalance)
                .eq(AppUserBalance::getOpenId, openId));
        BigDecimal lastBalance = appUserBalance == null ? BigDecimal.ZERO : appUserBalance.getBalance();
        AppUserBalanceRecord balanceRecord = new AppUserBalanceRecord();
        balanceRecord.setOpenId(openId);
        balanceRecord.setAmount(amount);
        balanceRecord.setTradeNo(tradeNo);
        balanceRecord.setLastAmount(lastBalance);
        balanceRecord.setRemainingAmount(amount);
        balanceRecord.setRefundableAmount(amount);
        balanceRecord.setType(AppUserBalanceRecordEnum.RECHARGE.getCode());
        balanceRecord.setStatus(BalanceRecordStatusEnum.PROCESSING.getCode());
        balanceRecord.setPayment(payment);
        userBalanceRecordMapper.insert(balanceRecord);
    }


    /**
     * 创建jsapi的service
     */
    private JsapiServiceExtension createJsapiService() {
        if (config.isHttpProxyEnabled()) {
            HttpClient httpClient = new DefaultHttpClientBuilder()
                    .config(config.getPayConfig())
                    .readTimeoutMs(config.getReadTimeoutMs())
                    .connectTimeoutMs(config.getConnectTimeoutMs())
                    .writeTimeoutMs(config.getWriteTimeoutMs())
                    .proxy(new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(config.getHttpProxyHost(), config.getHttpProxyPort())))
                    .build();
            // 设置商户配置，并使用 httpClientBuilder 设置 HttpClient 所需的网络配置
            return new JsapiServiceExtension.Builder().httpClient(httpClient).config(config.getPayConfig()).build();
        }

        return new JsapiServiceExtension.Builder().config(config.getPayConfig()).build();
    }

    /**
     * 创建支付请求
     *
     * @param payRequest 支付请求
     * @return 构造好的支付请求
     */
    private PrepayRequest createPrepayRequest(UnifiedPayRequest payRequest) {
        PrepayRequest request = new PrepayRequest();

        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp("111.34.209.135"); // 传入真实用户IP
        request.setSceneInfo(sceneInfo); // 关联到支付请求

        // 必填APP信息
        request.setAppid(config.getAppId());
        request.setMchid(config.getMerchantId());
        request.setNotifyUrl(config.getRechargeNotifyUrl());
        request.setDescription(payRequest.getDescription());
        request.setOutTradeNo(payRequest.getOutTradeNo());
        // 过期时间，为取消订单做准备
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(config.getExpireMinute());
        ZonedDateTime zonedDateTime = expireTime.atZone(ZoneId.systemDefault());
        // 定义格式化模式，包括时区信息
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        request.setTimeExpire(zonedDateTime.format(formatter));
        // 金额
        Amount amountInfo = new Amount();
        Integer amount = AmountUtil.convertY2Cent(payRequest.getAmount()).intValue();
        amountInfo.setTotal(amount);
        request.setAmount(amountInfo);
        // 分账信息
        SettleInfo settleInfo = new SettleInfo();
        settleInfo.setProfitSharing(config.isProfitSharing());
        request.setSettleInfo(settleInfo);
        // 付款人信息
        Payer payer = new Payer();
        payer.setOpenid(payRequest.getOpenId());
        request.setPayer(payer);
        return request;
    }

    /**
     * 创建并且保存支付日志
     *
     * @param address       请求地址
     * @param desc          描述
     * @param request       请求参数
     * @param response      返回参数
     * @param success       是否成功
     * @param callDirection 调用方向
     * @param errMap        错误信息Map
     */
    private void createAndSaveEsPayApiLog(String address, String desc, Object request,
            Object response, boolean success, CallDirectionEnum callDirection,
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
            apiLog.setCallDirection(callDirection.getName());
            apiLog.setPayApiSource(PayApiSourceEnum.WECHAT_PAY.getName());
            apiLog.setErrorMsg(errString);
            apiLog.setCreateTime(LocalDateTime.now());
            logService.saveLogs(apiLog);
        } catch (Exception e) {
            log.error("保存微信支付相关日志失败:{}", e.getMessage(), e);
        }
    }

    /**
     * 退款前校验
     *
     * @param openId 用户的openid
     * @param amount 退款金额
     */
    private BigDecimal checkBeforeRefund(String openId, BigDecimal amount) {
        boolean unpaid = orderService.existsUnpaidOrders(openId);
        if (unpaid) {
            throw new BaseException("存在未支付/完成的订单,请先支付!");
        }
        // 校验用户余额是否够退款
        AppUserBalance appUserBalance = userBalanceMapper
                .selectOne(
                        Wrappers.<AppUserBalance>lambdaQuery()
                                .select(AppUserBalance::getId, AppUserBalance::getBalance)
                                .eq(AppUserBalance::getOpenId, openId));

        BigDecimal userBalance = appUserBalance.getBalance();
        if (userBalance.compareTo(amount) < 0) {
            throw new RuntimeException(MessageConstants.USER_BALANCE_NOT_ENOUGH);
        }
        return userBalance;
    }

    /**
     * 创建退款请求
     *
     * @param request         退款请求包装实体
     * @param rechargeRecords 充值记录
     * @param openId          用户open id
     */
    private List<UnifiedRefundRequest> createUnifiedRefundRequest(RefundRequest request,
            List<AppUserBalanceRecord> rechargeRecords,
            String openId) {
        List<UnifiedRefundRequest> refundList = new ArrayList<>();
        BigDecimal refundAmount = request.getAmount();
        for (AppUserBalanceRecord rechargeRecord : rechargeRecords) {
            if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            // 生成退款单号
            String tradeNo = OrderNoUtils.generateWithdrawalOrderNo();
            UnifiedRefundRequest refundRequest = new UnifiedRefundRequest();
            refundRequest.setPayment(rechargeRecord.getPayment());
            refundRequest.setSource(request.getPaySource());
            refundRequest.setRefundNo(tradeNo);
            refundRequest.setRefundReason("提现退款");
            refundRequest.setTradeNo(rechargeRecord.getOutTradeNo());
            refundRequest.setBizTradeNo(rechargeRecord.getTradeNo());
            refundRequest.setOpenId(openId);
            refundRequest.setPayOrderAmount(rechargeRecord.getAmount());
            BigDecimal refundableAmount = rechargeRecord.getRefundableAmount();
            // 判断可退金额是否大于退款金额
            if (refundableAmount.compareTo(refundAmount) >= 0) {
                refundRequest.setAmount(refundAmount);
                rechargeRecord.setRefundableAmount(refundableAmount.subtract(refundAmount));
                // 可退款金额大于退款金额，则是部分退款
                if (refundableAmount.compareTo(refundAmount) > 0) {
                    // 充值订单是部分退款
                    String outRequestNo = rechargeRecord.getOutRequestNo();
                    if (ObjectUtils.isEmpty(outRequestNo)) {
                        outRequestNo = OrderNoUtils.generateOutRequestNo();
                    }
                    // 退款请求号,部分退款时必填
                    refundRequest.setOutRequestNo(outRequestNo);
                }
                // 需要退款的金额置零
                refundAmount = BigDecimal.ZERO;
            } else {
                // 充值订单不够退款金额，则本次充值单是全退
                refundRequest.setAmount(refundableAmount);
                rechargeRecord.setRefundableAmount(BigDecimal.ZERO);
                refundAmount = refundAmount.subtract(refundableAmount);
            }
            userBalanceRecordMapper.updateById(rechargeRecord);
            refundList.add(refundRequest);
        }
        return refundList;
    }

    /**
     * 为用户创建退款记录,类型是提现
     *
     * @param openId       用户openid
     * @param amount       退款金额
     * @param tradeNo      系统生成的退款单号
     * @param outTradeNo   三方生成的订单号
     * @param outRequestNo 退款请求号，针对部分退款时需要
     */
    private void createRefundRecord(String openId, BigDecimal amount, String tradeNo, String outTradeNo,
            String outRequestNo, String payTradeNo,Payment payment) {
        // 创建退款记录
        AppUserBalanceRecord balanceRecord = new AppUserBalanceRecord();
        balanceRecord.setPayment(payment);
        balanceRecord.setAmount(amount);
        balanceRecord.setOutTradeNo(outTradeNo);
        balanceRecord.setOpenId(openId);
        balanceRecord.setTradeNo(tradeNo);
        balanceRecord.setType(AppUserBalanceRecordEnum.WITHDRAW.getCode());
        balanceRecord.setOutRequestNo(outRequestNo);
        balanceRecord.setPayTradeNo(payTradeNo);
        balanceRecord.setStatus( BalanceRecordStatusEnum.PROCESSING.getCode());

        try {
                 userBalanceRecordMapper.insert(balanceRecord);
        } catch (Exception e) {
           log.error("创建退款记录失败:{}", e.getMessage(), e);
        }
   
    }

    /**
     * 创建退款的service
     */
    private RefundService createRefundService() {
        if (config.isHttpProxyEnabled()) {
            HttpClient httpClient = new DefaultHttpClientBuilder()
                    .config(config.getPayConfig())
                    .readTimeoutMs(config.getReadTimeoutMs())
                    .connectTimeoutMs(config.getConnectTimeoutMs())
                    .writeTimeoutMs(config.getWriteTimeoutMs())
                    .proxy(new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(config.getHttpProxyHost(), config.getHttpProxyPort())))
                    .build();
            // 设置商户配置，并使用 httpClientBuilder 设置 HttpClient 所需的网络配置
            // 退款的不需要再执行.config
            return new RefundService.Builder().httpClient(httpClient).build();
        }
        return new RefundService.Builder().config(config.getPayConfig()).build();
    }

    /**
     * 创建退款请求
     *
     * @param request 退款请求实体
     * @return 构造完成的退款请求
     */
    private CreateRequest createRefundRequest(UnifiedRefundRequest request) {
        CreateRequest refundRequest = new CreateRequest();
        // 【微信支付订单号】 微信支付侧订单的唯一标识
        refundRequest.setTransactionId(request.getTradeNo());
        // 【商户订单号】 服务商下单时传入的服务商系统内部订单号。
        // transaction_id和out_trade_no必须二选一进行传参。
        refundRequest.setOutTradeNo(request.getBizTradeNo());
        // 【商户退款单号】 服务商系统内部的退款单号
        refundRequest.setOutRefundNo(request.getRefundNo());
        refundRequest.setReason(request.getRefundReason());
        refundRequest.setNotifyUrl(config.getRefundNotifyUrl());
        AmountReq amountReq = new AmountReq();
        Long amount = AmountUtil.convertY2Cent(request.getAmount());
        amountReq.setRefund(amount);
        Long payAmount = AmountUtil.convertY2Cent(request.getPayOrderAmount());
        amountReq.setTotal(payAmount);
        amountReq.setCurrency("CNY");
        refundRequest.setAmount(amountReq);
        return refundRequest;
    }

}
