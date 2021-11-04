package com.imooc.oa.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.oa.entity.User;
import com.imooc.oa.service.UserService;
import com.imooc.oa.service.exception.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
* 接受来自前端的数据
* */
@WebServlet(name = "LoginServlet", value = "/check_login")
public class LoginServlet extends HttpServlet {
    private UserService userServlet = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = LoggerFactory.getLogger(LoginServlet.class);
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Map<String, Object> result = new HashMap<>();
        try {
            //调用业务逻辑
            User user = userServlet.checkLogin(username, password);
            HttpSession session = request.getSession();
            //向session存入用户登录信息，属性名：login_user
            session.setAttribute("login_user", user);
            result.put("code", "0");
            result.put("message","success");
            result.put("redirect_url", "/index");
        } catch (BussinessException ex) {
            logger.error(ex.getMessage(), ex);
            result.put("code", ex.getCode());
            result.put("message",ex.getMessage());
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.put("code", ex.getClass().getSimpleName());
            result.put("message",ex.getMessage());
        }
        //转换为json字符串
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }
}
