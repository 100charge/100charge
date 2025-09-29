package com.xingchuan.common.constant;

/**
 * 通用消息常量信息
 */
public class MessageConstants {

    public static final String NOT_OBTAINED_LOGIN_INFO = "未获取到登录信息,请重新登录!";

    public static final String USER_DOES_NOT_EXIST = "用户不存在";
    public static final String ROLE_DOES_NOT_EXIST = "角色不存在";
    public static final String PHONE_NUMBER_IS_EXIST = "用户手机号已存在";
    public static final String USERNAME_IS_EXIST = "用户名已被他人使用";
    public static final String USER_BALANCE_NOT_ENOUGH = "用户余额不足";
    public static final String ALLIN_PAY_BALANCE_NOT_ENOUGH = "通联用户余额不足";
    public static final String SEND_CMD_MSG_ERROR = "发送命令失败，请重试";
    public static final String USER_DISABLED = "用户已被禁用";
    public static final String NOT_PLATFORM_USER = "用户不是平台用户";
    public static final String NOT_OPERATOR_USER = "用户不是运营商用户";
    public static final String AREA_ERROR = "地区错误";
    public static final String TENANT_NOT_EXIST = "运营商不存在";
    public static final String TENANT_DISABLE = "运营商已停用";
    public static final String TENANT_NAME_IS_EXIST = "运营商名称已被使用";
    public static final String TENANT_CODE_IS_EXIST = "运营商编号已被使用";
    public static final String TENANT_COMPANY_NOT_EXIST = "运营商企业信息不存在";
    public static final String PLEASE_SELECT_TENANT = "请选择运营商";
    public static final String OPERATOR_EXISTS = "运营商已存在";

    public static final String RULE_NOT_EXIST = "计费规则不存在";
    public static final String RULE_TIME_EXISTS = "计费规则时段已存在";
    public static final String RULE_TIME_NOT_EXIST = "计费规则时段不存在";
    public static final String START_TIME_CANNOT_BE_LATER_THAN_END_TIME = "营业开始时间不能大于营业结束时间";
    public static final String START_TIME_LATER_THAN_END_TIME = "开始时间不能大于或等于结束时间";

    public static final String CHARGING_STATIONS_NOT_EXIST = "场站信息不存在";
    public static final String STATION_CLOSED = "场站未开放";
    public static final String CHARGING_STATIONS_EXIST = "场站信息已存在";
    public static final String CHARGING_STATIONS_REMOVED = "操作失败,场站已被禁用或已删除!";
    public static final String PORT_NOT_AUTHENTICATED = "场站端口号未认证";
    public static final String DEVICE_INFO_EXCEPTION = "设备信息异常";

    public static final String PLEASE_SCAB_THE_CORRERCT_QR_CODE = "请扫描正确的二维码";

    public static final String CHARGING_PILE_NOT_EXIST = "充电桩不存在!";
    public static final String CHARGING_GUN_NOT_EXIST = "充电枪不存在!";
    public static final String CHARGER_DISABLED = "充电桩未启用";
    public static final String CHARGING_PILE_NUMBER_REPEAT = "充电桩设备编号重复!";
    public static final String CHARGING_PILE_EXIST_ORDER = "此充电桩已存在订单,无法删除!";
    public static final String CHARGING_GUN_EXIST_ORDER = "此充电枪有已存在订单,无法删除!";
    public static final String DEVICE_OFFLINE = "设备已离线";
    public static final String CHARGING_PILE_EXIST_GUNS = "充电桩下存在充电枪";
    public static final String DEVICE_NOT_CHARGING = "设备未在充电中，无法停止充电";
    public static final String DEVICE_NOT_PLUGGED_IN = "设备未插枪";
    public static final String CHARGING_START_FAILED = "启动充电失败";


    public static final String RULE_TIME_PERIOD_QTY_ERROR = "收费时间段必须是48条!";
    public static final String RULE_TIME_PERIOD_REPEAT = "收费时间段不能重复!";

    public static final String DEPT_NOT_EXIST = "部门不存在!";
    public static final String DEPT_NAME_EXIST = "操作失败，部门名称已存在!";
    public static final String DEPT_PHONE_EXIST = "操作失败，手机号已存在!";
    public static final String DEPT_CODE_EXIST = "操作失败，社会机构代码已存在!";
    public static final String PARENT_DEPT_DISABLE = "操作失败,父级部门已停用!";
    public static final String PARENT_DEPT_NOT_EXIST = "操作失败,父级部门不存在!";

    public static final String FLEET_NOT_EXIST = "车队不存在!";
    public static final String COMPANY_DISABLE = "操作失败,企业已停用!";
    public static final String CURRENT_CHARGING_GUN_UNAVAILABLE = "当前充电枪不可用";
    public static final String STATION_CANNOT_BE_REPEATED = "场站不能重复!";

    public static final String FLEET_DISABLE = "操作失败,车队已停用!";
    public static final String CAR_NOT_EXIST = "车辆信息不存在!";
    public static final String CAR_PLATE_EXIST = "操作失败，车牌号已存在!";
    public static final String VIN_EXIST = "操作失败，VIN已存在!";
    public static final String CAR_EXIST_ORDER = "当前车辆存在订单,无法删除!";
    public static final String INVALID_LICENSE_PLATE_MESSAGE = "车牌信息不符合规范";
    public static final String INVALID_VIN_MESSAGE = "车辆VIN信息不符合规范";
    public static final String DEFAULT_VEHICLE_EXIST = "默认车辆已存在";
    public static final String VEHICLE_NOT_DUPLICATE_CERTIFICATION = "车辆不可重复认证";

    public static final String PROMOTION_NOT_EXIST = "优惠券信息不存在，或已过期";
    public static final String APPLICABLE_SCOPE_NOT_EXIST = "优惠券适用范围不存在";
    public static final String COUPON_NOT_APPLICABLE_FOR_STATION = "优惠券场站不适用";
    public static final String COUPON_ALREADY_USED = "优惠券已被使用";
    public static final String PROMOTION_EXPIRED = "优惠已过期";
    public static final String PROMOTION_NOT_STARTED = "优惠已未开始";
    public static final String DISCOUNT_RATIO_IS_EMPTY = "优惠券类型为折扣券时,折扣百分比不能为空或小于0";
    public static final String PROMOTION_AMOUNT_IS_EMPTY = "优惠券类型为满减券或直减券时,优惠券金额不能为空或小于0";
    public static final String SATISFY_CONDITION_IS_EMPTY = "优惠券类型为满减券时,满足条件不能为空或小于0";
    public static final String NON_DISCOUNT_VOUCHER_CANNOT_SET_MAX_DISCOUNT = "非折扣券不允许设置最大抵扣金额";


    public static final String MARKETING_STRATEGY_NOT_EXIST = "营销策略不存在";
    public static final String MEET_CONDITION_REPEAT = "存在满足条件相同的优惠策略";
    public static final String MEET_CONDITION_IS_EMPTY = "使用场景是充电或充值,满足条件不能为空或小于0";
    public static final String ACTIVITY_NOT_IN_VALID_PERIOD = "活动不在有效期内";
    public static final String ACTIVITY_EXPIRED = "活动已失效";
    public static final String ACTIVITY_EXHAUSTED = "活动次数已用尽";
    public static final String USER_EXHAUSTED = "用户次数已用尽";
    public static final String ENTERPRISE_USERS_NOT_ALLOWED = "该活动不对企业用户开放";


    public static final String USER_NOT_EXIST = "用户不存在!";
    public static final String USER_IS_DISABLE = "用户账号已停用!";
    public static final String USER_NON_ENTERPRISE = "该用户不属于企业用户!";
    public static final String USER_NOT_BELONG_TO_ENTERPRISE = "用户不属于当前企业!";

    public static final String ACCOUNT_EXISTS_MESSAGE = "该账号已存在";
    public static final String ACCOUNT_NOT_EXIST = "该账号不存在";
    public static final String ACCOUNT_ALREADY_IN_USE = "该账号已被使用";

    public static final String COMPANY_BALANCE_INSUFFCIENT = "企业余额不足,请先充值企业余额!";
    public static final String RECHARGE_AMOUNT_LESS_THAN_MINIMUM = "充值金额小于最低充值金额";


    public static final String TENANT_IS_DISABLE = "运营商已被禁用!";
    public static final String TENANT_IS_EXPIRED = "运营商已过期!";
    public static final String COMPANY_NOT_BELONG_TO_OPERATORS = "企业不属于当前运营商!";

    public static final String STATION_EXIST_ORDER = "当前场站已存在订单,无法删除!";

    public static final String ORDER_NOT_EXIST = "订单不存在!";
    public static final String ORDER_NOT_STARTED = "订单未开始";
    public static final String CHARGING_ORDER_TIMEOUT = "充电订单超时";
    public static final String ORDER_ENDED_OR_EXCEPTIONAL = "订单已结束或异常订单";
    public static final String ORDER_PAYMENT_STATUS_EXCEPTION = "订单支付状态异常";
    public static final String ORDER_NOT_COMPLETE_OR_EXCEPTION = "订单未结束或订单异常";
    public static final String EXIST_PENDING_PAYMENT_ORDERS = "当前存在未支付的订单,请支付后再启动充电";
    public static final String ERROR_CHARGING_TIME_LIMIT_NOT_EXCEEDED = "启动充电未超过90秒，不允许停止";

    public static final String PARAM_ERROR = "参数有误!";

    public static final String BANNER_NOT_EXIST = "当前banner不存在";

    public static final String SHARE_RATIO_NOT_EXIST = "分成比例不存在!";
    public static final String SHARE_RATIO_IS_EXIST = "该场站已经存在分成比例,请勿重复设置!";
    public static final String SHARE_RATIO_EXIST_UNSETTLED_BILL = "该分成比例还存在未结算的账单,请勿禁用或删除!";

    public static final String USER_WITHDRAWAL_RECORD_NOT_EXIST = "用户提现记录不存在!";
    public static final String WITHDRAWAL_REVIEW_COMPLETED = "用户提现记录已经审核完成,不要重复审核!";
    public static final String REFUND_FAIL = "退款失败!";
    public static final String USER_RECHARGE_RECORD_NOT_EXIST = "用户充值记录不存在!";
    public static final String REFUNDABLE_AMOUNT_INSUFFICIENT = "充值订单的可退款金额不足,请重新选择!";
    public static final String THIS_TIME_REFUND_AMOUNT_GREATER_THAN_WITHDRAWAL_AMOUNT = "本次退款金额大于用户提现金额,请检查!";
    public static final String REFUND_COMPLETED = "退款已完成,请不要重复退款!";
    public static final String REFUND_AMOUNT_GREATER_THAN_WITHDRAWAL_AMOUNT = "处理中金额+已退款金额+本次退款金额,大于提现金额,请检查!";
    public static final String REFUND_STATUS_NOT_REFUND_IN_PROGRESS = "当前提现申请的退款状态不是退款中,无法退款!";
    public static final String WITHDRAWAL_REVIEW_NOT_PASSED = "当前提现申请审核未通过,无法退款!";
    public static final String WITHDRAWAL_AMOUNT_MUST_BE_GREATER_THAN_ZERO = "提现金额必须大于0";
    public static final String WITHDRAWAL_AMOUNT_MUST_BE_GREATER_THAN_OR_EQUAL_TO_100 = "提现金额不得超出最低提现金额";
    public static final String PLATFORM_WALLET_INFO_EXCEPTION = "平台钱包信息异常";
    public static final String PLATFORM_WALLET_NOT_ENOUGH = "平台钱包金额不足";

    public static final String SERVICE_FEE_NOT_MEET_COUPON_USE_CONDITIONS = "当前订单服务费金额未满足优惠券使用条件!";
    public static final String NOT_MEET_COUPON_USE_CONDITIONS = "当前订单电费+服务费金额未满足优惠券使用条件!";
    /**
     * 微信错误码 字段
     */
    public static final String WECHAT_ERR_CODE = "errcode";

    public static final String AGENT_NOT_CREATED_MEMBER = "该代理商未创建会员";
    public static final String AGENT_HAS_CREATED_MEMBER = "代理商已创建过会员";
    public static final String BUSINESS_CERTIFICATION_MESSAGE = "该代理商企业信息认证中或已认证";
    public static final String BUSINESS_LICENSE_CERTIFICATION_MESSAGE = "该代理商企业信息未认证或营业执照已认证成功";
    public static final String ID_CARD_FRONT_CERTIFICATION_ERROR_MESSAGE = "该运营商营业执照未认证成功或身份证正面已认证成功";
    public static final String ID_CARD_BACK_CERTIFICATION_ERROR_MESSAGE = "该运营商身份证正面未认证成功或身份证反面已认证成功";
    public static final String SEND_VERIFICATION_CODE_ERROR_MESSAGE = "该运营商身份证反面未认证成功或手机号已绑定";
    public static final String MERCHANT_SEND_VERIFICATION_CODE_ERROR_MESSAGE = "该运营商未实名认证或手机号已绑定";
    public static final String VERIFICATION_CODE_REMAINING_VALID_TIME = "验证码剩余有效时间 :";
    public static final String COPY_FAILURE_MESSAGE = "影印件获取失败";
    public static final String VERIFICATION_CODE_EXPIRED_MESSAGE = "验证码已过期，请重试";
    public static final String WITHDRAWAL_AGREEMENT_SIGN_ERROR_MESSAGE = "该代理商未绑定手机号或已认签约成功";
    public static final String AUTHENTICATE_REAL_NAME_ERROR_MESSAGE = "改代理商类型异常";
    public static final String BIND_BANK_CARD_ERROR_MESSAGE = "该运营商未绑定手机号或银行卡已绑定";

    public static final String SIGNING_TENANT_NOT_EXIST = "签约租户不存在";
    public static final String SIGNING_FAILURE = "签约失败";

    public static final String CARD_NOT_EXIST = "卡不存在:";
    public static final String CARD_NUMBER_EXIST = "卡号已存在:";
    public static final String CARD_NAME_EXIST = "卡名称已存在:";
    public static final String COMPANY_NOT_EXIST = "企业不存在:";
    public static final String CARD_USED = "存在充电中的订单,不能禁用：";
    public static final String CARD_NUMBER_NOT_NULL = "卡号不能为空";
    public static final String CARD_TYPE_NOT_NULL = "卡类型不能为空";
    public static final String CARD_WHITE_LIST = "存在白名单中,无法禁用：";
    public static final String CARD_ALREADY_IN_WHITELIST = "卡已在白名单中：";
    public static final String CARD_ALREADY_ACTIVATED = "状态为待激活的卡才能激活,请检查卡状态:";
    public static final String CARD_STATUS_ABNORMAL = "状态为正常的卡才能加入白名单,请检查卡状态:";
    public static final String CARD_NUMBER_REPEAT = "卡号重复:";
    public static final String IMPORT_ERROR = "Excel没有数据,无法导入";
    public static final String VIN_REPEAT = "VIN重复:";
    public static final String PLATE_NUM_REPEAT = "车牌号重复:";
    public static final String PLATE_NUM_IS_EMPTY = "车牌号不能为空";
    public static final String VIN_IS_EMPTY = "VIN不能为空";
    public static final String PLATE_NUM_ERROR = "请检查车牌号是否正确:";
    public static final String VIN_ERROR = "请检查VIN是否正确:";

    public static final String IMPORT_FAILURE = "导入失败";
    public static final String CHARGING_GUN_NUMBER_MAX = "充电枪编号已达到最大值:";
    public static final String CHARGER_CHARGING = "充电桩正在充电";
    public static final String GROUP_NAME_EXISTS = "分组名称已存在";
    public static final String GROUP_NOT_EXISTS = "分组不存在";
    public static final String GROUP_NOT_ENABLE = "分组未启用";
    public static final String CARD_NOT_NORMAL = "存在状态非正常的卡,请重新选择";
    public static final String CARD_NOT_FROZEN = "存在状态非已冻结的卡,请重新选择";
    public static final String CARD_NOT_ACTIVATED = "存在待激活的卡,请重新选择";
    public static final String CARD_ACTIVATED = "存在已激活的卡,请重新选择";
    public static final String FILE_IS_NULL = "文件不能为空";

    public static final String RULE_ALREADY_BOUND = "计费规则已绑定场站,无法删除";
    public static final String STATION_EXIST_PILE = "场站已有充电桩,无法删除";

    public static final String COMPANY_INCONSISTENT_WITH_CHARGING_STATION_OPERATOR = "当前账户无法在此场站充电,请联系管理员";

    public static final String REJECT_REASON_IS_NULL = "请填写驳回原因!";
    public static final String VEHICLE_CERTIFIED = "车辆已认证,无法修改车牌号";
    public static final String STATUS_NOT_ALLOW_REVIEW = "当前状态无法认证审核";
    public static final String VEHICLE_UNDER_REVIEW = "车辆信息审核中,请勿重复提交";

    public static final String ACCOUNT_NOT_EXISTS = "通联账户不存在";
    public static final String ACCOUNT_ALREADY_VERIFIED = "当前账户已实名认证成功";
    public static final String ACCOUNT_NOT_VERIFIED = "当前账户未实名认证,请先实名认证";
    public static final String ACCOUNT_ALREADY_BINDING_PHONE = "当前账户已绑定手机号";
    public static final String NOT_AVAILABLE_ALLIN_ACCOUNT = "充值失败,请联系客服人员";
    public static final String INSUFFICIENT_RECHARGE_FREQUENCY = "本日充值次数已用尽,请联系客服人员";
    public static final String RECHARGE_AMOUNT_MORE_THAN_MAXIMUM = "单笔充值金额大于最大充值金额:";
    public static final String DAILY_RECHARGE_FREQUENCY_RUN_OUT = "充值失败,单日充值次数已用尽";
    public static final String RULE_TIME_PERIOD_ERROR = "下一时段的开始时间必须等于上一时段的结束时间";

    public static final String ORDER_NOT_FOUND_MESSAGE = "订单不存在";
    public static final String SPLIT_AMOUNT_MISMATCH_MESSAGE = "充值订单剩余可分账金额与平台不一致";
    public static final String USER_ALLOCATION_EXCEEDS_ALLOWED_AMOUNT = "分账金额超出应分金额";


    public static final String FLEET_NOT_BELONG_TO_CURRENT_COMPANY = "车队不属于当前企业";
    public static final String CAR_HAS_BEEN_BOUND = "车辆已被绑定";
    public static final String EXIST_UNFINISHED_ORDER = "操作失败,当前用户或车辆存在未完成的订单";
    public static final String FLEET_NAME_EXISTS = "车队名称已被使用";
    public static final String FLEET_BALANCE_NOT_ENOUGH = "车队余额不足";
    public static final String MEMBER_ID_IS_NULL = "通联会员id为空";

    public static final String OPERATOR_ID_IS_EXIST = "商户已被绑定";
    public static final String SECRET_KEY_IS_EMPTY = "密钥不能为空";
    public static final String REACH_MAXIMUM_START_ORDERS = "当前账号进行中的订单已达到限制数量";
    public static final String RESP_URL_NOT_NULL = "响应地址不能为空";
    public static final String REQ_URL_NOT_NULL = "请求地址不能为空";
    public static final String URL_NOT_NULL = "请求和响应地址不能为空";
    public static final String GATE_BRAND_NOT_EXIST = "道闸信息不存在";
    public static final String INSUFFICIENT_REFUND_AMOUNT_ERROR = "可退款金额异常，请联系管理员";

    public static final String ORDER_EXCEPTION_OR_INVOICED = "订单异常或订单已开票";

    public static final String OPERATOR_NOT_EXIST = "商户不存在";

    public static final String TAX_NUMBER_IS_REQUIRED = "企业开票信息，税号必填";
    public static final String TAX_COMPANY_IS_EXISTS = "企业开票信息，发票抬头已存在";
    public static final String INVOICE_ERROR = "开票失败，请联系平台人员";
    public static final String INVOICE_CONFIG_NOT_EXIST = "开票信息不存在";
    public static final String NO_OPERATION_REQUIRED = "没有需要下发的充电枪";
    public static final String SEND_QR_CODE_ERROR = "二维码下发失败,请稍后再试";

    public static final String CANNOT_BE_UPDATE_STATION_TENANT = "非自有场站，无法修改运营商";


    public static final String INVOICE_NOT_EXIST = "未找到开票信息!";
    public static final String INVOICE_ITEM_NOT_EXIST = "未找到开票明细信息!";
    public static final String INVOICE_TITLE_NOT_EXIST = "未找到发票抬头!";
    public static final String GUN_NUMBER_ERROR = "%s%s桩只有一把枪";
    public static final String ORDER_TIME_LIMIT = "订单充电时间较短，请在12小时后再试";
    public static final String BLUE_INVOICE_NO_NOT_EXIST = "原蓝票编号不存在，请更新原发票后再试";
    public static final String INVOICE_ITEM_IS_EMPTY = "开票项为空或电费服务费为0";
    public static final String INVOICE_NOT_BLUE_INVOICE = "原发票类型不是蓝票，无法进行红冲";
    public static final String INVOICE_NOT_NORMAL = "原发票未开具完成";
    public static final String RED_INVOICE_EXIST = "已创建红色发票，请不要重复开具";
}
