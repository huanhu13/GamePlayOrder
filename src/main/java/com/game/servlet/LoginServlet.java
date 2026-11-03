package com.game.servlet;

import com.game.entity.User;
import com.game.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录控制器：GET 显示登录页，POST 处理登录
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            User user = userService.login(username, password);
            // 登录成功：写入会话并跳转首页
            HttpSession session = req.getSession();
            session.setAttribute("loginUser", user);
            // 消除过滤器留下的提示信息，避免重复显示
            session.removeAttribute("msg");
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            // 登录失败：回显错误信息
            req.setAttribute("msg", e.getMessage());
            req.setAttribute("username", username);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
