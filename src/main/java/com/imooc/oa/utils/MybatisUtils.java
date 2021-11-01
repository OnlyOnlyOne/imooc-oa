package com.imooc.oa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

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
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }

    }


    /**
     * Execute query object.
     * 执行SELECT查询SQL
     * @param func the func 要执行查询语句的代码块
     * @return the object 查询结果
     */
    public static Object executeQuery(Function<SqlSession, Object> func) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = func.apply(sqlSession);
            return obj;
        }finally {
            sqlSession.close();
        }
    }


    /**
     * Execute update object.
     *
     * @param func 要执行的写操作代码块
     * @return 写操作后返回的结果
     * 做到update的时候应该联想到事务的commit和rollbcak
     *
     */
    public static Object executeUpdate(Function<SqlSession, Object> func) {
        //里面的参数是autoCommit:false.就可以进行事务的手动提交
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try {
            Object obj = func.apply(sqlSession);
            sqlSession.commit();
            return obj;
        } catch (RuntimeException e) {
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
