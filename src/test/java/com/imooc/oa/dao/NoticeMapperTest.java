package com.imooc.oa.dao;

import com.imooc.oa.entity.Notice;
import com.imooc.oa.utils.MybatisUtils;
import junit.framework.TestCase;

import java.util.Date;

public class NoticeMapperTest extends TestCase {

    public void testInsertSelective() {
        MybatisUtils.executeUpdate(sqlSession -> {
            NoticeMapper dao = sqlSession.getMapper(NoticeMapper.class);
            Notice notice = new Notice();
            notice.setReceiverId(2l);
            notice.setContent("测试消息");
            notice.setCreateTime(new Date());
            dao.insertSelective(notice);
            return null;
        });
    }
}