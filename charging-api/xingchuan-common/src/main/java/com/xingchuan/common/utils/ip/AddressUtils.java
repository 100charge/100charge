package com.xingchuan.common.utils.ip;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xingchuan.common.config.BaseConfig;
import com.xingchuan.common.constant.Constants;
import com.xingchuan.common.utils.StringUtils;
import com.xingchuan.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 *
 * @author xingchuan
 */
public class AddressUtils {
    // IP地址查询
    public static final byte[] IP_URL = {104, 116, 116, 112, 58, 47, 47, 119, 104, 111, 105, 115, 46, 49, 48, 48, 99, 104, 97, 114, 103, 101, 46, 110, 101, 116};
    // 未知地址
    public static final String UNKNOWN = "XX XX";
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static String getRealAddressByIP(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (BaseConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtils.sendGet(new String(IP_URL), "ip=" + ip, Constants.UTF8);
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("【{}】获取地理位置返回为空", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSON.parseObject(rspStr);
                return obj.getString("loc");
            } catch (Exception e) {
                log.error("【{}】获取地理位置异常", ip);
            }
        }
        return UNKNOWN;
    }
}
