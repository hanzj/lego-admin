package com.lego.sharding.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.dto.DTO;
import com.lego.core.gen.converter.BooleanConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingConfigInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "生效", converter = BooleanConverter.class)
    private boolean enable;
    @ExcelProperty(value = "描述")
    private String description;
}
