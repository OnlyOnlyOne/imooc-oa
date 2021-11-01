package com.imooc.oa.utils.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DruidDataSourceFactory extends UnpooledDataSourceFactory {
    public DruidDataSourceFactory() {
        //DruidDataSource是阿里云的一个包含数据源的类。
        //this.datasource是继承类的属性
        this.dataSource = new DruidDataSource();
    }

    @Override
    public DataSource getDataSource() {
        try {
            ((DruidDataSource)this.dataSource).init();//初始化Druid数据源
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return this.dataSource;
    }
}
