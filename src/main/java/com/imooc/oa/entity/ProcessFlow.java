package com.imooc.oa.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessFlow {
    /**
    * 处理任务编号
    */
    private Long processId;

    /**
    * 表单编号
    */
    private Long formId;

    /**
    * 经办人编号
    */
    private Long operatorId;

    /**
    * apply-申请  audit-审批
    */
    private String action;

    /**
    * approved-同意 refused-驳回
    */
    private String result;

    /**
    * 审批意见
    */
    private String reason;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 审批时间
    */
    private Date auditTime;

    /**
    * 任务序号
    */
    private Integer orderNo;

    /**
    * ready-准备 process-正在处理 complete-处理完成 cancel-取消
    */
    private String state;

    /**
    * 是否最后节点,0-否 1-是
    */
    private Integer isLast;
}