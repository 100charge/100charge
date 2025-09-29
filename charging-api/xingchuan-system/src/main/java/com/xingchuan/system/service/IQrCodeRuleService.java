package com.xingchuan.system.service;


import com.xingchuan.charging.domain.resp.QRCodeResult;

public interface IQrCodeRuleService {

    QRCodeResult parseQRCode(String url);
}
