package com.lego.core.action;

import com.lego.core.data.ActionType;
import com.lego.core.data.ICommonService;
import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.data.hibernate.ICommonDao;
import com.lego.core.data.hibernate.IGenericDao;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ActionVO;
import com.lego.core.web.LegoBeanFactory;

import java.util.List;

public abstract class MaintainAction {

    protected String operatorCode;
    protected String permissionCode;
    protected String description;
    protected ICommonDao commonDao = LegoBeanFactory.getBean(ICommonDao.class);

    protected MaintainAction(String permissionCode, String operatorCode) {
        this.operatorCode = operatorCode;
        this.permissionCode = permissionCode;
    }

    public final String run() {
        preprocess();
        doRun();
        postprocess();
        createLog();
        return description;
    }

    protected void createLog() {
        if (StringUtil.isNotBlank(description)) {
            ActionVO actionVO = new ActionVO();
            actionVO.setEntityCode(getEntityCode());
            actionVO.setEntityName(getEntityName());
            actionVO.setActionType(getActionType());
            actionVO.setDescription(description);
            actionVO.setOperatorCode(operatorCode);
            actionVO.setPermissionCode(permissionCode);
            LegoBeanFactory.getBean(ICommonService.class).addLog(actionVO);
        }
    }

    protected abstract void doRun();

    protected void preprocess() {
    }

    protected void postprocess() {
    }

    protected abstract ActionType getActionType();

    protected String getEntityCode() {
        return null;
    }

    protected abstract String getEntityName();

    protected <T extends BaseEntity> T findByCode(Class<T> clazz, String code) {
        return commonDao.findByCode(clazz, code);
    }

    protected <T extends BaseEntity> T findByUnsureCode(Class<T> clazz, String code) {
        return commonDao.findByUnsureCode(clazz, code);
    }

    protected <T extends BaseEntity> List<T> findByCodes(Class<T> clazz, List<String> codes) {
        return commonDao.findByCodes(clazz, codes);
    }

    protected <D extends IGenericDao<?>> D getDao(Class<D> clazz) {
        return LegoBeanFactory.getBean(clazz);
    }
}
