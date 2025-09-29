package com.xingchuan.charging.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingchuan.charging.domain.entity.ChargingGuns;
import com.xingchuan.charging.mapper.ChargingGunsMapper;
import com.xingchuan.charging.service.IChargingGunsService;
import org.springframework.stereotype.Service;

/**
 * 充电桩-充电枪Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class ChargingGunsServiceImpl extends ServiceImpl<ChargingGunsMapper, ChargingGuns> implements IChargingGunsService {
}
