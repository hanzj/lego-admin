package com.lego.system.entity;

import com.lego.core.data.TreeEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.system.entity.simpletype.SysPermissionRouteType;
import com.lego.system.entity.simpletype.SysPermissionType;
import com.lego.system.vo.SysPermissionTypeCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "sys_permission")
public class SysPermission extends TreeEntity<SysPermission> {

    private int sn;

    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private SysPermissionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_type_id", referencedColumnName = "id")
    private SysPermissionRouteType routeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private SysCustomForm form;

    private String relateCode;

    protected SysPermission() {
    }

    public SysPermission(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        attributes.put("上级菜单", EntityUtil.toString(getParent()));
        attributes.put("序号", StringUtil.toString(sn));
        attributes.put("类型", EntityUtil.getName(type));
        attributes.put("路由类型", EntityUtil.getName(routeType));
        attributes.put("表单", EntityUtil.toString(form));
        attributes.put("图标", icon);
        attributes.put("关联编码", relateCode);
    }

    public boolean isMenu() {
        return SysPermissionTypeCode.MENU.equals(type.getCode());
    }

    public boolean isReport() {
        return SysPermissionTypeCode.REPORT.equals(type.getCode());
    }
}
