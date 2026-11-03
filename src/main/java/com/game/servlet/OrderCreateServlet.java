package com.game.servlet;

import com.game.entity.User;
import com.game.service.OrderService;
import com.game.service.PlaymateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 创建订单控制器：GET 显示下单页，POST 提交订单
 */
@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet {

    private OrderService orderService = new OrderService();
    private PlaymateService playmateService = new PlaymateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 查询陪玩师信息用于页面展示
        try {
            int playmateId = Integer.parseInt(req.getParameter("playmateId"));
            req.setAttribute("playmate", playmateService.findById(playmateId));
        } catch (Exception e) {
            req.setAttribute("msg", "参数错误");
        }
        req.getRequestDispatcher("/order_create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        String playmateId = req.getParameter("playmateId");
        String appointTime = req.getParameter("appointTime");
        String hours = req.getParameter("hours");
        String remark = req.getParameter("remark");
        try {
            orderService.create(loginUser, Integer.parseInt(playmateId), appointTime, hours, remark);
            // 下单成功：跳转我的订单
            resp.sendRedirect(req.getContextPath() + "/order/list");
        } catch (NumberFormatException e) {
            req.setAttribute("msg", "参数格式错误");
            forwardBack(req, resp, playmateId, appointTime, hours, remark);
        } catch (Exception e) {
            // 下单失败：回显错误与已填内容
            req.setAttribute("msg", e.getMessage());
            forwardBack(req, resp, playmateId, appointTime, hours, remark);
        }
    }

    /** 回显表单并返回下单页 */
    private void forwardBack(HttpServletRequest req, HttpServletResponse resp,
                             String playmateId, String appointTime, String hours, String remark)
            throws ServletException, IOException {
        req.setAttribute("playmateId", playmateId);
        req.setAttribute("appointTime", appointTime);
        req.setAttribute("hours", hours);
        req.setAttribute("remark", remark);
        try {
            req.setAttribute("playmate", playmateService.findById(Integer.parseInt(playmateId)));
        } catch (Exception ignored) { }
        req.getRequestDispatcher("/order_create.jsp").forward(req, resp);
    }
}
