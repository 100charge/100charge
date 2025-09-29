package com.xingchuan.charging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退款状态枚举
 */
@Getter
@AllArgsConstructor
public enum RefundStatusEnum {
    PENDING_REFUND(0, "待退款"),
    REFUND_IN_PROGRESS(1, "退款中"),
    REFUND_COMPLETED(2, "退款完成");

    private final Integer code;
    private final String desc;

    public static RefundStatusEnum getEnumByCode(Integer code) {
        for (RefundStatusEnum e : RefundStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        for (RefundStatusEnum e : RefundStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e.desc;
            }
        }
        return null;
    }


    // 审核状态
    @Getter
    @AllArgsConstructor
    public enum ReviewStatusEnum {
        PENDING_REVIEW(0, "待审核"),
        APPROVED(1, "审核通过"),
        REVIEW_REJECTION(2, "审核驳回");

        private final Integer code;
        private final String desc;

        public static ReviewStatusEnum getEnumByCode(Integer code) {
            for (ReviewStatusEnum e : ReviewStatusEnum.values()) {
                if (e.code.equals(code)) {
                    return e;
                }
            }
            return null;
        }

        public static String getDescByCode(Integer code) {
            for (ReviewStatusEnum e : ReviewStatusEnum.values()) {
                if (e.code.equals(code)) {
                    return e.desc;
                }
            }
            return null;
        }
    }

}