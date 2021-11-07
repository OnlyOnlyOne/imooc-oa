package com.imooc.oa.dao;

import com.imooc.oa.entity.ProcessFlow;

public interface ProcessFlowMapper {
    int deleteByPrimaryKey(Long processId);

    int insert(ProcessFlow record);

    int insertSelective(ProcessFlow record);

    ProcessFlow selectByPrimaryKey(Long processId);

    int updateByPrimaryKeySelective(ProcessFlow record);

    int updateByPrimaryKey(ProcessFlow record);
}