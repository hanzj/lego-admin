package com.lego.system.service;

import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysRoleInfo;
import com.lego.system.vo.SysDataScopeAuthVO;
import com.lego.system.vo.SysPermissionAuthVO;
import com.lego.system.vo.SysRoleCreateVO;
import com.lego.system.vo.SysRoleModifyVO;
import com.lego.system.vo.SysRoleSearchVO;

import java.util.List;

public interface ISysRoleService {

    void add(String operatorCode, SysRoleCreateVO vo);

    void modify(String operatorCode, SysRoleModifyVO vo);

    void auth(String operatorCode, SysPermissionAuthVO vo);

    void authDataScope(String operatorCode, SysDataScopeAuthVO vo);

    List<SysRoleInfo> findBy(SysRoleSearchVO vo);

    SysRoleInfo findByCode(String code);

    void deleteBy(String code);

    List<TypeInfo> findSimpleType(String name, String operatorCode);
}
