package com.imooc.oa.dao;

import com.imooc.oa.entity.ProcessFlow;

import java.util.List;

public interface ProcessFlowMapper {
    int deleteByPrimaryKey(Long processId);

    int insert(ProcessFlow record);

    int insertSelective(ProcessFlow record);

    ProcessFlow selectByPrimaryKey(Long processId);

    int updateByPrimaryKeySelective(ProcessFlow record);

    int updateByPrimaryKey(ProcessFlow record);

    List<ProcessFlow> selectByFormId(Long formId);
}