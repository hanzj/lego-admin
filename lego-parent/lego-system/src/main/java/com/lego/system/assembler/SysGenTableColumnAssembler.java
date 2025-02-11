package com.lego.system.assembler;

import com.lego.core.assembler.EntityAssembler;
import com.lego.system.dto.SysGenTableColumnInfo;
import com.lego.system.entity.SysGenTable;
import com.lego.system.entity.SysGenTableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysGenTableColumnAssembler extends EntityAssembler<SysGenTableColumnInfo, SysGenTableColumn> {

  @Autowired
  private SysGenTableAssembler tableAssembler;

  @Override
  protected SysGenTableColumnInfo doCreate(SysGenTableColumn entity) {
    SysGenTableColumnInfo info = new SysGenTableColumnInfo();
    info.setSn(entity.getSn());
    info.setCode(entity.getCode());
    info.setComment(entity.getComment());
    info.setCreator(createTypeInfo(entity.getCreator()));
    info.setDataType(entity.getDataType());
    info.setFormType(entity.getFormType());
    info.setJavaField(entity.getJavaField());
    info.setJavaFieldType(entity.getJavaFieldType());
    info.setAttributes(entity.getAttributes());
    info.setName(entity.getName());
    info.setRequired(entity.isRequired());
    info.setTable(createTypeInfo(entity.getTable()));
    SysGenTable relativeTable = entity.getRelativeTable();
    if (relativeTable != null) {
      info.setRelativeTable(createTypeInfo(relativeTable));
    }
    info.setUnique(entity.isUniqueness());
    return info;
  }

}
