package com.imooc.oa.dao;

import com.imooc.oa.entity.User;
import com.imooc.oa.utils.MybatisUtils;
import org.apache.ibatis.jdbc.SQL;

public class UserDao {
    /**
     * Select by username user.
     *
     * @param username the username
     * @return 返回一个查询的用户，null则为不存在
     */
    public User selectByUsername(String username) {
        User user = (User) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("usermapper.selectByUsername", username));
        return user;
    }

}
