package com.lego.core.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum GenericConditionEnum {

    EQUALS("equals", "等于", "="),
    NOT_EQUALS("notEquals", "不等于", "<>"),

    LIKE("like", "模糊匹配", "LIKE"),

    CONTAINS("contains", "包含", "IN"),
    NOT_CONTAINS("notContains", "不包含", "NOT IN"),

    START_WITH("startWith", "开始于", "START WITH"),
    END_WITH("endWith", "结束于", "END WITH"),

    IS_NULL("isNull", "为空", "IS NULL"),
    IS_NOT_NULL("isNotNull", "不为空", "IS NOT NULL"),

    GREATER_THEN("greaterThan", "大于", ">"),
    GREATER_THEN_OR_EQUALS("greaterThanOrEquals", "大于等于", ">="),

    LESS_THAN("lessThan", "小于", "<"),
    LESS_THAN_OR_EQUALS("lessThanOrEquals", "小于等于", "<=");

    private static final List<GenericConditionEnum> notValueTypes = Arrays.asList(IS_NULL, IS_NOT_NULL);
    private static final List<GenericConditionEnum> multiValueTypes = Arrays.asList(CONTAINS, NOT_CONTAINS);

    private String code;
    private String name;
    private String type;

    private GenericConditionEnum(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public boolean equals(String code) {
        return this.getCode().equals(code);
    }

    public boolean equals(GenericConditionEnum condition) {
        return this.getCode().equals(condition.getCode());
    }

    public boolean needValue() {
        return !notValueTypes.contains(this);
    }

    public boolean isSingleValue() {
        return !multiValueTypes.contains(this);
    }

    public static GenericConditionEnum get(String code) {
        for (GenericConditionEnum value : values()) {
            if (value.equals(code)) {
                return value;
            }
        }
        return null;
    }

}
