package com.imooc.oa.dao;

import com.imooc.oa.entity.ProcessFlow;
import com.imooc.oa.utils.MybatisUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;


public class ProcessFlowMapperTest extends TestCase {
    @Test
    public void testInsert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowMapper dao = sqlSession.getMapper(ProcessFlowMapper.class);
            ProcessFlow flow = new ProcessFlow();
            flow.setFormId(3l);
            flow.setOperatorId(2l);
            flow.setAction("audit");
            flow.setReason("approved");
            flow.setReason("同意");
            flow.setCreateTime(new Date());
            flow.setAuditTime(new Date());
            flow.setOrderNo(1);
            flow.setState("ready");
            flow.setIsLast(1);
            dao.insert(flow);
            return null;
        });
    }

}