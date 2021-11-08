package com.imooc.oa.service;

import com.imooc.oa.dao.EmployeeDao;
import com.imooc.oa.dao.LeaveFormMapper;
import com.imooc.oa.dao.ProcessFlowMapper;
import com.imooc.oa.entity.Employee;
import com.imooc.oa.entity.LeaveForm;
import com.imooc.oa.entity.ProcessFlow;
import com.imooc.oa.service.exception.BussinessException;
import com.imooc.oa.utils.MybatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
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
                String noticeContent = String.format("您的请假申请[%s-%s]已提交,请等待上级审批."
                        , sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));

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

//    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
//        return (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
//            LeaveFormMapper dao = sqlSession.getMapper(LeaveFormMapper.class);
//            List<Map> formList = dao.selectByParams(pfState, operatorId);
//            return formList;
//
//        });
//    }
    /**
     * 获取指定任务状态及指定经办人对应的请假单列表
     * @param pfState ProcessFlow任务状态
     * @param operatorId 经办人编号
     * @return 请假单及相关数据列表
     */
    public List<Map> getLeaveFormList(String pfState , Long operatorId){
        return (List<Map>)MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormMapper dao = sqlSession.getMapper(LeaveFormMapper.class);
            List<Map> formList = dao.selectByParams(pfState, operatorId);
            return formList;
        });
    }

    public void audit(Long formId, Long operatorId, String result, String reason) {
        MybatisUtils.executeUpdate(sqlSession -> {
            //1.无论同意/驳回,当前任务状态变更为complete
            ProcessFlowMapper processFlowMapper = sqlSession.getMapper(ProcessFlowMapper.class);
            List<ProcessFlow> flowList = processFlowMapper.selectByFormId(formId);
            if (flowList.size() == 0) {
                throw new BussinessException("PF001", "无效的审批流程");
            }
            List<ProcessFlow> processList = flowList.stream().filter(p -> p.getOperatorId() == operatorId && p.getState().equals("process")).collect(Collectors.toList());
            ProcessFlow process = null;
            if (processList.size() == 0) {
                throw new BussinessException("PF002", "未找到待处理任务");
            } else {
                process =  processList.get(0);
                process.setState("complete");
                process.setResult(result);
                process.setReason(reason);
                process.setAuditTime(new Date());
                processFlowMapper.updateByPrimaryKey(process);
            }

            LeaveFormMapper leaveFormDao = sqlSession.getMapper(LeaveFormMapper.class);
            LeaveForm form = leaveFormDao.selectByPrimaryKey(formId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());//表单提交人信息
            Employee operator = employeeDao.selectById(operatorId);//任务经办人信息
            //NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            //2.如果当前任务是最后一个节点,代表流程结束,更新请假单状态为对应的approved/refused
            if (process.getIsLast() == 1) {
                form.setState(result);//approved|refused
                leaveFormDao.updateByPrimaryKey(form);

                String strResult = null;
                if (result.equals("approved")) {
                    strResult = "批准";
                } else if (result.equals("refused")) {
                    strResult = "驳回";
                }
                String noticeContent = String.format("您的请假申请[%s-%s]%s%s已%s,审批意见:%s,审批流程已结束",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                        operator.getTitle(), operator.getName(),
                        strResult, reason);//发给表单提交人的通知
//                noticeDao.insert(new Notice(form.getEmployeeId(),noticeContent));
//
//                noticeContent = String.format("%s-%s提起请假申请[%s-%s]您已%s,审批意见:%s,审批流程已结束" ,
//                        employee.getTitle() , employee.getName() , sdf.format( form.getStartTime()) , sdf.format(form.getEndTime()),
//                        strResult , reason);//发给审批人的通知
//                noticeDao.insert(new Notice(operator.getEmployeeId(),noticeContent));
            } else {
                List<ProcessFlow> readyList = flowList.stream().filter(p -> p.getState().equals("ready")).collect(Collectors.toList());
                //3.如果当前任务不是最后一个节点且审批通过,那下一个节点的状态从ready变为process
                if (result.equals("approved")) {
                    ProcessFlow readyProcess = readyList.get(0);
                    readyProcess.setState("process");
                    processFlowMapper.updateByPrimaryKey(readyProcess);
                } else if (result.equals("refused")) {
                    //4.如果当前任务不是最后一个节点且审批驳回,则后续所有任务状态变为cancel,请假单状态变为refused
                    for(ProcessFlow p:readyList){
                        p.setState("cancel");
                        processFlowMapper.updateByPrimaryKey(p);
                    }
                    form.setState("refused");
                    leaveFormDao.updateByPrimaryKey(form);
                }
            }
            return null;
        });

    }
}
