package com.lego.sharding.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.dto.BusDTO;
import com.lego.core.dto.TypeInfo;
import com.lego.core.module.gen.converter.BooleanConverter;
import com.lego.core.module.gen.converter.TypeInfoConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingTableInfo extends BusDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "生效", converter = BooleanConverter.class)
    private boolean enable;
    @ExcelProperty(value = "描述")
    private String description;
    @ExcelProperty(value = "逻辑表名称")
    private String logicTableName;
    @ExcelProperty(value = "物理表表达式")
    private String actualDataNodes;
    @ExcelProperty(value = "本片字段")
    private String shardingColumn;
    @ExcelProperty(value = "算法", converter = TypeInfoConverter.class)
    private TypeInfo algorithm;
    @ExcelProperty(value = "数据源", converter = TypeInfoConverter.class)
    private TypeInfo dataSource;
    @ExcelProperty(value = "模板", converter = TypeInfoConverter.class)
    private TypeInfo template;
    @ExcelProperty(value = "规则", converter = TypeInfoConverter.class)
    private TypeInfo config;
}
