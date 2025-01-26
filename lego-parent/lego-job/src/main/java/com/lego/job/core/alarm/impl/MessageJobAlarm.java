package com.lego.job.core.alarm.impl;

import com.lego.core.common.Constants;
import com.lego.core.data.ICommonService;
import com.lego.core.module.job.biz.model.ReturnT;
import com.lego.core.vo.MessageCreateVO;
import com.lego.core.enums.MessageTypeEnum;
import com.lego.job.core.alarm.JobAlarm;
import com.lego.job.core.conf.XxlJobAdminConfig;
import com.lego.job.core.model.XxlJobGroup;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.core.model.XxlJobLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Slf4j
@Component
public class MessageJobAlarm implements JobAlarm {

    @Resource
    private ICommonService commonService;

    @Override
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog) {
        boolean alarmResult = true;
        if (info != null) {
            String alarmContent = "Alarm Job LogId=" + jobLog.getId();
            if (jobLog.getTriggerCode() != ReturnT.SUCCESS_CODE) {
                alarmContent += "<br>TriggerMsg=<br>" + jobLog.getTriggerMsg();
            }
            if (jobLog.getHandleCode() > 0 && jobLog.getHandleCode() != ReturnT.SUCCESS_CODE) {
                alarmContent += "<br>HandleCode=" + jobLog.getHandleMsg();
            }

            XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupMapper().load(Integer.valueOf(info.getJobGroup()));
            String content = MessageFormat.format(loadEmailJobAlarmTemplate(),
                group != null ? group.getTitle() : "null",
                info.getId(),
                info.getJobDesc(),
                alarmContent);

            MessageCreateVO messageVO = new MessageCreateVO();
            messageVO.setRecipient(info.getAuthor());
            messageVO.setCreator(Constants.ADMIN_EMPLOYEE_CODE);
            messageVO.setName("任务调度中心监控报警，《" + group.getTitle() + "》执行任务《" + info.getJobDesc() + "》异常");
            messageVO.setType(MessageTypeEnum.FORM.getCode());
            messageVO.setContent(content);
            commonService.addSysMessage(messageVO);
        }

        return true;
    }

    private static final String loadEmailJobAlarmTemplate() {
        String mailBodyTemplate = "<h5>监控告警明细：</span>" +
            "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:100%;\" >\n" +
            "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
            "      <tr>\n" +
            "         <td width=\"20%\" >执行器</td>\n" +
            "         <td width=\"10%\" >任务ID</td>\n" +
            "         <td width=\"20%\" >任务描述</td>\n" +
            "         <td width=\"10%\" >告警类型</td>\n" +
            "         <td width=\"40%\" >告警内容</td>\n" +
            "      </tr>\n" +
            "   </thead>\n" +
            "   <tbody>\n" +
            "      <tr>\n" +
            "         <td>{0}</td>\n" +
            "         <td>{1}</td>\n" +
            "         <td>{2}</td>\n" +
            "         <td>调度失败</td>\n" +
            "         <td>{3}</td>\n" +
            "      </tr>\n" +
            "   </tbody>\n" +
            "</table>";

        return mailBodyTemplate;
    }
}
