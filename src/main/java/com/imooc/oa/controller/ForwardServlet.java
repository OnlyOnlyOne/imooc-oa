package com.imooc.oa.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
//为显示页面提供服务
@WebServlet(name = "ForwardServlet", value = "/forward/*")
public class ForwardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        /**
         * /forward/form
         * /forward/a/b/c/form
         */

        String subUri = uri.substring(1);
        String page = subUri.substring(subUri.lastIndexOf("/"));
        request.getRequestDispatcher(page + ".ftl").forward(request,response);
    }


}
