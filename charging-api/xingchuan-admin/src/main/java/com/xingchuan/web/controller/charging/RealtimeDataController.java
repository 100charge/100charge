package com.xingchuan.web.controller.charging;

import com.alibaba.fastjson2.JSON;
import com.xingchuan.charging.domain.model.ChargingRealtimeData;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.redis.RedisCache;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "实时数据（桩的实时状态）")
@RequestMapping(value = "/realtime-data")
public class RealtimeDataController {

    @Resource
    private RedisCache redisCache;

    @GetMapping("/status")
    public AjaxResult query(@RequestParam("operatorId") String operatorId,
                            @RequestParam("deviceNo") String deviceNo,
                            @RequestParam("gunNo") String gunNo) {
        String key = String.format(CacheConstants.DEVICE_STATUS_KEY, operatorId, deviceNo, gunNo);
        String jsonStr = redisCache.getCacheObject(key);
        ChargingRealtimeData realtimeData = JSON.parseObject(jsonStr, ChargingRealtimeData.class);
        return AjaxResult.success(realtimeData);
    }
}
