package com.xingchuan.charging.domain.model;

import com.xingchuan.charging.enums.CMDEnums;
import com.xingchuan.charging.enums.ProtocolTypeEnum;
import lombok.Data;

/**
 * 设备CMD命令类
 */
@Data
public class DeviceCMDMsg {
    /**
     * 消息的key
     */
    private String keys;
    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 系统生成的订单号
     */
    private String tradeNo;
    /**
     * 枪号
     */
    private String GunNo;
    /**
     * 命令
     */
    private CMDEnums cmd;
    /**
     * 协议版本
     */
    private ProtocolTypeEnum protocol;
    /**
     * 二维码地址
     */
    private String qrUrl;

    public DeviceCMDMsg() {
    }

    public DeviceCMDMsg(String keys, String deviceNo, String tradeNo, String gunNo, CMDEnums cmd, ProtocolTypeEnum protocol) {
        this.keys = keys;
        this.deviceNo = deviceNo;
        this.tradeNo = tradeNo;
        this.GunNo = gunNo;
        this.cmd = cmd;
        this.protocol = protocol;
    }
}
