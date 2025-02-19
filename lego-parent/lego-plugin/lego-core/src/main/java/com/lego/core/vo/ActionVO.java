package com.lego.core.vo;

import com.lego.core.enums.ActionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionVO extends VO {

    private static final long serialVersionUID = 1L;

    private String entityCode;
    private String entityName;
    private String operatorCode;
    private String permissionCode;
    private ActionType actionType;
    private String description;
}
