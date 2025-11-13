package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.NoticeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通知公告表 sys_notice
 *
 * @author zhitan
 */
@Data
@ApiModel(value = "获取文章详情返回")
public class NoticeDetailResponse {

    /**
     * 公告ID
     */
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long noticeId;

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;

    /**
     * 公告类型（1:通知, 2:公告,3:文章）
     */
    @ApiModelProperty(value = "公告类型")
    private String noticeType;

    /**
     * 公告类型描述（1:通知, 2:公告,3:文章）
     */
    @ApiModelProperty(value = "公告类型描述")
    private String noticeTypeDesc;

    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;

    public String getNoticeTypeDesc() {
        return noticeTypeDesc = NoticeTypeEnum.getDescByCode(noticeType);
    }
}
