package com.imooc.oa.utils;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MyBatisUtils工具类，创建全局唯一的SqlSessionFactory
 */
public class MybatisUtils {
    //利用static(静态)属于类不属于对象 ,保证全局唯一
    private static SqlSessionFactory sqlSessionFactory = null;
}
