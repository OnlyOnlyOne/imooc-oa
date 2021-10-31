package com.imooc.oa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * MyBatisUtils工具类，创建全局唯一的SqlSessionFactory
 */
public class MybatisUtils {
    //利用static(静态)属于类不属于对象 ,保证全局唯一
    private static SqlSessionFactory sqlSessionFactory = null;
    //static块用于初始化静态对象
    static {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    }
}
