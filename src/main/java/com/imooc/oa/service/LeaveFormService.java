package com.imooc.oa.service;

import com.imooc.oa.dao.EmployeeDao;
import com.imooc.oa.dao.LeaveFormMapper;
import com.imooc.oa.dao.ProcessFlowMapper;
import com.imooc.oa.entity.Employee;
import com.imooc.oa.entity.LeaveForm;
import com.imooc.oa.entity.ProcessFlow;
import com.imooc.oa.utils.MybatisUtils;

import java.util.Date;

/*
* 请假单流程服务
* */
public class LeaveFormService {
    /**
     * Create 创建请假单
     *
     * @param form the form 前端输入的请假单数据
     * @return the leave form 持久化后的请假单对象
     */
    public LeaveForm createLeaveForm(LeaveForm form) {

        LeaveForm savedForm = (LeaveForm)MybatisUtils.executeUpdate(sqlSession -> {
            //1.持久化form表单数据，8级以下员工为processing 8级为approved
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) {
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormMapper leaveFormMapper = sqlSession.getMapper(LeaveFormMapper.class);
            leaveFormMapper.insertSelective(form);
            //2.增加第二条流程数据，说明表单已经提交，状态为complete
            ProcessFlowMapper processFlowMapper = sqlSession.getMapper(ProcessFlowMapper.class);
            ProcessFlow flow1 = new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(employee.getEmployeeId());
            flow1.setAction("apply");
            flow1.setCreateTime(new Date());
            flow1.setOrderNo(1);
            flow1.setState("complete");
            flow1.setIsLast(0);
            processFlowMapper.insert(flow1);
            //分情况创建其余流程数据

            //3.1 7级以下的员工，生成部门经理审批任务，请假时间大于36小时，还需生成总经理审批任务
            if (employee.getLevel() < 7) {
                Employee dmanager = employeeDao.selectLeader(employee);
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setState("process");
                long diff = form.getEndTime().getTime() - form.getStartTime().getTime();
                float hours = diff/(1000*60*60)*1f;
                if (hours >= BussinessConstants.MANAGET_AUDIT_HOURS) {
                    flow2.setIsLast(0);
                    processFlowMapper.insert(flow2);
                    Employee manager = employeeDao.selectLeader(dmanager);
                    ProcessFlow flow3 = new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manager.getEmployeeId());
                    flow3.setAction("aduit");
                    flow3.setCreateTime(new Date());
                    flow3.setOrderNo(3);
                    flow3.setState("reday");
                    flow3.setIsLast(1);
                    processFlowMapper.insert(flow3);

                } else {
                    flow2.setIsLast(1);
                    processFlowMapper.insert(flow2);
                }

            } else if (employee.getLevel() == 7) {
                Employee manager = employeeDao.selectLeader(employee);
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(manager.getEmployeeId());
                flow.setAction("aduit");
                flow.setCreateTime(new Date());
                flow.setOrderNo(2);
                flow.setState("process");
                flow.setIsLast(1);
                processFlowMapper.insert(flow);
            } else if (employee.getLevel() == 8) {
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(employee.getEmployeeId());
                flow.setAction("aduit");
                flow.setResult("approved");
                flow.setReason("自动通过");
                flow.setCreateTime(new Date());
                flow.setAuditTime(new Date());
                flow.setOrderNo(2);
                flow.setState("complete");
                flow.setIsLast(1);
                processFlowMapper.insert(flow);
            }
            //3.2 7级员工 生成总经理审批任务
            //3.3 8级员工 生成总经理审批任务，系统自动通过
            return form;
        });
        return savedForm;
    }
}
