package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.domain.resp.QRCodeResult;
import com.xingchuan.system.service.IQrCodeRuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/qrCode")
public class QrCodeRuleController {

    @Resource
    private IQrCodeRuleService qrCodeRuleService;

    @GetMapping("/parseQRCode")
    public QRCodeResult parseQRCode(@RequestParam("url") String url) {
        return qrCodeRuleService.parseQRCode(url);
    }
}
