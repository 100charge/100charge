package com.xingchuan.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.QrCodeRule;
import com.xingchuan.charging.domain.resp.QRCodeResult;
import com.xingchuan.charging.mapper.QrCodeRuleMapper;
import com.xingchuan.system.service.IQrCodeRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;

@Service
public class QrCodeRuleServiceImpl extends ServiceImpl<QrCodeRuleMapper, QrCodeRule> implements IQrCodeRuleService {

    /**
     * 二维码解析
     *
     * @param url 二维码地址
     */
    @Override
    public QRCodeResult parseQRCode(String url) {
        QRCodeResult result = new QRCodeResult();

        List<QrCodeRule> qrCodeRuleList = baseMapper.selectList(Wrappers.lambdaQuery());
        if (qrCodeRuleList.isEmpty()) {
            return result;
        }
        for (QrCodeRule rule : qrCodeRuleList) {
            Matcher matcher = rule.getPattern().matcher(url);
            if (matcher.find()) {
                if (rule.getPileGroup() != null) {
                    result.setDeviceNo(matcher.group(rule.getPileGroup()));
                }
                if (rule.getGunGroup() != null) {
                    String gunNo = matcher.group(rule.getGunGroup());
                    if (gunNo.length() < 2) {
                        gunNo = String.format("%02d", Integer.parseInt(gunNo));
                    }
                    result.setGunsNo(gunNo);
                }
                break;
            }
        }
        return result;
    }
}
