package com.lego.crm.vo;

import com.lego.core.vo.CreateVO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CrmLeadCreateVO extends CreateVO {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String mobile;
    private BigDecimal amount;
    private Date birthday;
    private String address;
    private int age;
    private BigDecimal size;
    private boolean status;
    private String email;
    private String employee;
    private String dept;
    private String source;
    private String customer;
}