package com.lego.system.action;

import com.lego.core.action.AddAction;
import com.lego.core.dto.MetaTableColumnInfo;
import com.lego.core.enums.FieldTypeEnum;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysGenTableColumnDao;
import com.lego.system.dao.ISysGenTableDao;
import com.lego.system.entity.SysGenTable;
import com.lego.system.entity.SysGenTableColumn;
import com.lego.system.vo.SysPermissionCode;

public class AddSysGenTableColumnInitAction extends AddAction<SysGenTableColumn, ISysGenTableColumnDao> {

    private String tableCode;
    private MetaTableColumnInfo columnInfo;

    private ISysGenTableDao tableDao = getDao(ISysGenTableDao.class);
    private ISysEmployeeDao employeeDao = getDao(ISysEmployeeDao.class);

    public AddSysGenTableColumnInitAction(String operatorCode, String tableCode, MetaTableColumnInfo columnInfo) {
        super(SysPermissionCode.manageGenTable, operatorCode);
        this.tableCode = tableCode;
        this.columnInfo = columnInfo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(columnInfo.getColumnName()), "列名不能为空！");
        BusinessException.check(StringUtil.isNotBlank(columnInfo.getColumnType()), "列类型不能为空！");
    }

    @Override
    protected SysGenTableColumn createTargetEntity() {
        SysGenTable table = tableDao.findByCode(tableCode);
        String dataType = getDataType(columnInfo.getColumnType());
        String columnName = columnInfo.getColumnName().toLowerCase();
        FieldTypeEnum field = FieldTypeEnum.getByDataType(dataType);

        SysGenTableColumn column = new SysGenTableColumn(columnName);
        column.setSn(columnInfo.getSort());
        column.setComment(columnInfo.getColumnComment());
        if (StringUtil.isBlank(columnInfo.getColumnComment())) {
            column.setComment(columnName);
        }
        column.setDataType(dataType);
        column.setFormType(field.getCode());
        column.setRequired(columnInfo.isRequired() || columnInfo.isPk());
        column.setUniqueness(columnInfo.isUnique() || columnInfo.isPk());
        column.setJavaField(getJavaField(columnName, field, table));
        column.setJavaFieldType(field.getJavaFieldType());
        column.setCreator(employeeDao.findByCode(operatorCode));
        column.setTable(table);
        return column;
    }

    private String getJavaField(String columnName, FieldTypeEnum field, SysGenTable table) {
        if (columnName.lastIndexOf("_id") > 0) {
            columnName = columnName.substring(0, columnName.indexOf("_id"));
        }
        if (field.isPublic() && columnName.lastIndexOf("_code") > 0) {
            columnName = columnName.substring(0, columnName.indexOf("_code"));
        }
        return StringUtil.toCamelCase(columnName, false);
    }

    private String getDataType(String columnType) {
        if (StringUtil.indexOf(columnType, "(") > 0) {
            return columnType.substring(0, columnType.indexOf("("));
        }
        return columnType;
    }
}
