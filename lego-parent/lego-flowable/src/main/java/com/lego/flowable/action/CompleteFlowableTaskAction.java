package com.lego.flowable.action;

import cn.hutool.core.convert.Convert;
import com.lego.core.data.ActionType;
import com.lego.core.exception.BusinessException;
import com.lego.core.feign.vo.TaskCompletedVO;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.core.util.StringUtil;
import com.lego.core.web.LegoBeanFactory;
import com.lego.flowable.handler.IFlowableCompleteHandler;
import com.lego.flowable.vo.FlowableCommentType;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.system.dao.ISysCustomFormDao;
import com.lego.system.entity.SysCustomForm;
import com.lego.system.entity.SysEmployee;
import com.lego.system.entity.SysGenTable;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.DelegationState;

public class CompleteFlowableTaskAction extends FlowableTaskAction {

    private FlowableTaskCompleteVO vo;

    private ISysCustomFormDao formDao = getDao(ISysCustomFormDao.class);

    private IFlowableCompleteHandler completeHandler = LegoBeanFactory.getBean(IFlowableCompleteHandler.class);

    public CompleteFlowableTaskAction(String operatorCode, FlowableTaskCompleteVO vo) {
        super(SysPermissionCode.oaUndo, operatorCode, vo.getId());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        super.preprocess();
        if (StringUtil.isNotBlank(task.getFormKey())) {
            BusinessException.check(!vo.getVariables().isEmpty(), "任务完工失败，请填写表单信息！");
            Object code = taskService.getVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY);
            if (code != null) {
                Object newCode = vo.getVariables().get(FlowableProcessConstants.FORM_UNIQUE_KEY);
                BusinessException.check(code.equals(newCode), "任务已保存表单数据[{0}]与新提交数据不一致，保存失败！", code);
            }
        }
    }

    @Override
    protected void doRun() {
        String commentType = FlowableCommentType.GENERIC.getCode();
        String comment = vo.getComment();
        BusinessException.check(StringUtil.isNotBlank(comment), "审批意见不能为空！");
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            SysEmployee employee = employeeDao.findByCode(operatorCode);
            comment = employee.getName() + ":" + comment;
        }
        taskService.addComment(vo.getId(), task.getProcessInstanceId(), commentType, comment);
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.resolveTask(vo.getId());
            return;
        }
        taskService.setAssignee(vo.getId(), operatorCode);
        if (StringUtil.isBlank(task.getFormKey())) {
            taskService.complete(vo.getId());
            return;
        }
        String code = processBusinessCallback();
        taskService.setVariableLocal(vo.getId(), FlowableProcessConstants.FORM_UNIQUE_KEY, code);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        String localScopeValue = modelAssembler.getUserTaskAttributeValue(bpmnModel, task.getTaskDefinitionKey(), FlowableProcessConstants.FORM_LOCAL_SCOPE);
        boolean localScope = Convert.toBool(localScopeValue, false);
        taskService.complete(vo.getId(), vo.getVariables(), localScope);
    }

    private String processBusinessCallback() {
        if (StringUtil.isBlank(task.getFormKey())) {
            return "";
        }
        SysCustomForm form = formDao.findByCode(task.getFormKey());
        SysGenTable genTable = form.getTable();
        BusinessException.check(genTable != null, "表单[{0}]无关联数据表，任务完工失败！", task.getFormKey());

        TaskCompletedVO completedVO = new TaskCompletedVO();
        completedVO.setTableCode(genTable.getCode());
        completedVO.setVariable(vo.getVariables());
        return completeHandler.doTaskCompleted(genTable.getAppCode(), completedVO);
    }

    @Override
    protected void postprocess() {
        new SendSysMessageAction(operatorCode, task.getProcessInstanceId()).run();
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.MODIFY;
    }
}
