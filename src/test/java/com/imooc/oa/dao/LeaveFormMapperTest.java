package com.imooc.oa.dao;

import com.imooc.oa.entity.LeaveForm;
import com.imooc.oa.utils.MybatisUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LeaveFormMapperTest extends TestCase {
    @Test
    public void testInsertSelective() {
        MybatisUtils.executeUpdate(sqlSession -> {
            LeaveFormMapper dao = sqlSession.getMapper(LeaveFormMapper.class);
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(4l); //员工编号
            form.setFormType(1); //事假
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null;//起始时间
            Date endTime = null;//结束时间
            try {
                startTime = sdf.parse("2020-03-25 08:00:00");
                endTime = sdf.parse("2020-04-01 18:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            form.setStartTime(startTime);
            form.setEndTime(endTime);
            form.setReason("回家探亲");//请假事由
            form.setCreateTime(new Date());//创建时间
            form.setState("processing");//当前状态
            dao.insertSelective(form);
            return null;
        });
    }

    @Test
    public void testSelectByParams() {
        MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormMapper dao = sqlSession.getMapper(LeaveFormMapper.class);
            List<Map> list = dao.selectByParams("process", 1l);
            System.out.println(list);
            return list;
        });
    }

}