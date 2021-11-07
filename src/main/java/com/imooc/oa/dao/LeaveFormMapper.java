package com.imooc.oa.dao;

import com.imooc.oa.entity.LeaveForm;

public interface LeaveFormMapper {
    int deleteByPrimaryKey(Long formId);

    int insert(LeaveForm record);

    int insertSelective(LeaveForm record);

    LeaveForm selectByPrimaryKey(Long formId);

    int updateByPrimaryKeySelective(LeaveForm record);

    int updateByPrimaryKey(LeaveForm record);
}