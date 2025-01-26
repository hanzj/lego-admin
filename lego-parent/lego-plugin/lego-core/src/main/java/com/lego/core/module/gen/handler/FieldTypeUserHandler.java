package com.lego.core.module.gen.handler;

import com.alibaba.fastjson.JSON;

public class FieldTypeUserHandler implements IFieldTypeHandler {

    @Override
    public String getComponentName() {
        return "FieldSelect";
    }

    @Override
    public Object parseDefaultValue(String value) {
        return JSON.parseObject(value, String.class);
    }

    @Override
    public Object parseSearchValue(String value) {
        return value;
    }

    @Override
    public String getJavaFieldType() {
        return String.class.getSimpleName();
    }

    @Override
    public boolean matchByType(String type) {
        return "user".equals(type);
    }

    @Override
    public boolean isPublic() {
        return true;
    }
}
