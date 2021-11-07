package com.imooc.oa.entity;

import java.util.Date;
import lombok.Data;

@Data
public class LeaveForm {
    /**
    * 请假单编号
    */
    private Long formId;

    /**
    * 员工编号
    */
    private Long employeeId;

    /**
    * 请假类型 1-事假 2-病假 3-工伤假 4-婚假 5-产假 6-丧假
    */
    private Integer formType;

    /**
    * 请假起始时间
    */
    private Date startTime;

    /**
    * 请假结束时间
    */
    private Date endTime;

    /**
    * 请假事由
    */
    private String reason;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * processing-正在审批 approved-审批已通过 refused-审批被驳回
    */
    private String state;
}