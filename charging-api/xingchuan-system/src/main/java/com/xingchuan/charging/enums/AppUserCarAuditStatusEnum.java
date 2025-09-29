package com.xingchuan.charging.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 车辆是否默认车量枚举
@Getter
@AllArgsConstructor
public enum AppUserCarAuditStatusEnum {

    /**
     * 是否默认车辆（否）
     */
    NO(0, "否"),
    /**
     * 是否默认车辆（是）
     */
    YES(1, "是");

    @EnumValue
    private final Integer code;
    private final String desc;

    public static String getDesc(Integer code) {
        for (AppUserCarAuditStatusEnum auditStatusEnum : AppUserCarAuditStatusEnum.values()) {
            if (auditStatusEnum.getCode().equals(code)) {
                return auditStatusEnum.getDesc();
            }
        }
        return null;
    }
}