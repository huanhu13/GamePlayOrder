package com.game.servlet;

import com.game.entity.User;
import com.game.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 订单列表控制器：按登录角色展示订单（普通用户/陪玩师/管理员）
 */
@WebServlet("/order/list")
public class OrderListServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        try {
            req.setAttribute("orderList", orderService.listForUser(loginUser));
        } catch (Exception e) {
            req.setAttribute("msg", "查询订单失败：" + e.getMessage());
        }
        req.getRequestDispatcher("/order_list.jsp").forward(req, resp);
    }
}
