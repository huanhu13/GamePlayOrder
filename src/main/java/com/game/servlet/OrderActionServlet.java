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
 * 订单操作控制器：统一处理 接单/开始服务/完成服务/取消订单
 * 通过 op 参数区分操作类型
 */
@WebServlet("/order/action")
public class OrderActionServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        String op = req.getParameter("op");
        int orderId;
        try {
            orderId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            req.setAttribute("msg", "订单参数错误");
            listAndForward(req, resp);
            return;
        }
        try {
            // 分发到对应业务方法
            if ("accept".equals(op)) {
                orderService.accept(loginUser, orderId);
            } else if ("start".equals(op)) {
                orderService.start(loginUser, orderId);
            } else if ("finish".equals(op)) {
                orderService.finish(loginUser, orderId);
            } else if ("cancel".equals(op)) {
                orderService.cancel(loginUser, orderId);
            } else {
                throw new IllegalArgumentException("未知操作");
            }
            // 操作成功：重定向回订单列表（防止重复提交）
            resp.sendRedirect(req.getContextPath() + "/order/list");
        } catch (Exception e) {
            req.setAttribute("msg", e.getMessage());
            listAndForward(req, resp);
        }
    }

    /** 重新查询列表并转发回列表页（用于展示错误信息） */
    private void listAndForward(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        try {
            req.setAttribute("orderList", new OrderService().listForUser(loginUser));
        } catch (Exception ignored) { }
        req.getRequestDispatcher("/order_list.jsp").forward(req, resp);
    }
}
