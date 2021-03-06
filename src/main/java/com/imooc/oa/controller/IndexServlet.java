package com.imooc.oa.controller;

import com.imooc.oa.entity.Department;
import com.imooc.oa.entity.Employee;
import com.imooc.oa.entity.Node;
import com.imooc.oa.entity.User;
import com.imooc.oa.service.DepartmentService;
import com.imooc.oa.service.EmployeeService;
import com.imooc.oa.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet", value = "/index")
public class IndexServlet extends HttpServlet {
    private UserService userService = new UserService();
    private EmployeeService employeeService = new EmployeeService();
    private DepartmentService departmentService = new DepartmentService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        //得到当前登录用户对象
        User user = (User) session.getAttribute("login_user");
        Employee employee = employeeService.selectById(user.getEmployeeId());
        Department department = departmentService.SelectById(employee.getDepartmentId());

        //获取登录用户可用功能模块列表
        List<Node> nodeList = userService.selectNodeByUserId(user.getUserId());
        //放入请求属性
        request.setAttribute("node_list", nodeList);
        session.setAttribute("current_employee",employee);
        session.setAttribute("current_department",department);
        //请求派发至ftl进行展现
        request.getRequestDispatcher("/index.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
