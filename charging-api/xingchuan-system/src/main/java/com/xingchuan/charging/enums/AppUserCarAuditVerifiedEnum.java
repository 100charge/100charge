package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 车辆认证枚举 0:未认证，1：审核中，2：已认证，3：认证失败
@Getter
@AllArgsConstructor
public enum AppUserCarAuditVerifiedEnum {

    /**
     * 未认证
     */
    NOT_CERTIFIED(0, "未认证"),
    /**
     * 审核中
     */
    UNDER_REVIEW(1, "审核中"),
    /**
     * 已认证
     */
    CERTIFIED(2, "已认证"),
    /**
     * 认证失败
     */
    CERTIFIED_FAIL(3, "认证失败");

    @EnumValue
    private final Integer code;
    private final String desc;

    public static String getDesc(Integer code) {
        for (AppUserCarAuditVerifiedEnum auditVerifiedEnum : AppUserCarAuditVerifiedEnum.values()) {
            if (auditVerifiedEnum.getCode().equals(code)) {
                return auditVerifiedEnum.getDesc();
            }
        }
        return null;
    }
}