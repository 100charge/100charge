package com.xingchuan.charging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingchuan.charging.domain.entity.ChargingPile;
import com.xingchuan.charging.domain.excel.ChargingPileExcelImport;
import com.xingchuan.charging.domain.req.ChargingPileAddRequest;
import com.xingchuan.charging.domain.req.ChargingPilePageListRequest;
import com.xingchuan.charging.domain.req.ChargingPileUpdateRequest;
import com.xingchuan.charging.domain.resp.ChargingPileDetailResponse;
import com.xingchuan.charging.domain.resp.ChargingPilePageListResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 充电桩Service接口
 *
 * @author ruoyi
 */
public interface IChargingPileService extends IService<ChargingPile> {

    /**
     * 查询充电桩列表
     */
    Page<ChargingPilePageListResponse> selectPageList(ChargingPilePageListRequest request);

    /**
     * 获取充电桩详情
     */
    ChargingPileDetailResponse getChargingPileDetail(Long id);

    /**
     * 新增充电桩
     */
    void addChargingPile(ChargingPileAddRequest request);

    /**
     * 修改充电桩
     */
    void updateChargingPile(ChargingPileUpdateRequest request);

    /**
     * 删除充电桩
     */
    void delChargingPile(Long id);

    /**
     * 新增充电枪
     */
    void addChargingGun(Long id);

    /**
     * 删除充电枪
     */
    void delChargingGun(Long id);

    /**
     * 导入充电桩数据
     *
     * @param pileList 桩数据
     * @return 结果
     */
    String importChargingPile(List<ChargingPileExcelImport> pileList);

    /**
     * 充电桩-修改启用、停用
     *
     * @param id 桩id
     */
    void updateShowStatus(Long id);

    /**
     * 平台-生成充电桩二维码
     *
     * @param deviceNo        桩编号
     * @param servletResponse 响应
     */
    void generateQRCodeZip(String deviceNo, HttpServletResponse servletResponse);
}
