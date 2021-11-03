package com.imooc.oa.service;

import com.imooc.oa.dao.RbacDao;
import com.imooc.oa.dao.UserDao;
import com.imooc.oa.entity.Node;
import com.imooc.oa.entity.User;
import com.imooc.oa.service.exception.BussinessException;

import java.util.List;

public class UserService {
    //private User user = null;

    private UserDao userDao = new UserDao();
    private RbacDao rbacDao = new RbacDao();
    /**
     * Check login user.
     * 根据前台输入进行登录校验
     *
     * @param username the username
     * @param password the password
     * @return the user 校验通过后，返回对应用户数据的User实体类
     * @throw BussinessException L001 - 用户名不存在 ，L002 - 密码错误
     */
    public User checkLogin(String username, String password) {
        User user = userDao.selectByUsername(username);
        if (user == null) {
            //抛出用户不存在异常
            throw new BussinessException("L001", "用户名不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new BussinessException("L002", "密码错误)");
        }

        return user;
    }

    public List<Node> selectNodeByUserId(Long userId) {
        List<Node> nodeList = rbacDao.selectNodeByUserId(userId);
        return nodeList;
    }
}
