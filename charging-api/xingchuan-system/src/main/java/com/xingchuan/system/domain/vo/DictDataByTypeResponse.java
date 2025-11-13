package com.xingchuan.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 字典数据表 sys_dict_data
 *
 * @author zhitan
 */
@Data
@ApiModel(value = "根据字典类型获取列表返回实体")
public class DictDataByTypeResponse {

    /**
     * 字典标签
     */
    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    /**
     * 字典键值
     */
    @ApiModelProperty(value = "字典键值")
    private String dictValue;


}
