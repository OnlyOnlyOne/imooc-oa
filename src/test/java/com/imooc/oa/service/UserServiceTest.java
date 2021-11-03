package com.imooc.oa.service;

import com.imooc.oa.entity.Node;
import com.imooc.oa.entity.User;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class UserServiceTest extends TestCase {
    private  UserService userService = new UserService();
    @Test

    public void testCheckLogin1() {
        userService.checkLogin("uu", "1234");
    }

    @Test
    public void testCheckLogin2() {
        userService.checkLogin("m8", "1234");
    }

    @Test
    public void testCheckLogin3() {
        User user = userService.checkLogin("m8", "test");
        System.out.println(user);
    }

    @Test
    public void testSelectNodeByUserId() {
       // List<Node> nodeList = userService.selectNodeByUserId();
    }
}