package com.imooc.oa.utils;

import org.junit.Test;

/**
 * The type Mybatis utils testor.
 */

public class MybatisUtilsTestor {
    @Test
    public void testcasel() {
        String output = (String)MybatisUtils.executeQuery(sqlSession -> {
            String result = (String) sqlSession.selectOne("test.sample");
            return result;
        });
        System.out.println(output);
    }

    @Test
    public void testcasel2() {
        String result = (String) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("test.sample"));
        System.out.println(result);
    }


}
