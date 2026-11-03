package com.game.servlet;

import com.game.entity.User;
import com.game.service.OrderService;
import com.game.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 评价控制器：GET 显示评价页，POST 提交评价
 */
@WebServlet("/review/add")
public class ReviewServlet extends HttpServlet {

    private ReviewService reviewService = new ReviewService();
    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            req.setAttribute("order", orderService.findById(orderId));
        } catch (Exception e) {
            req.setAttribute("msg", "参数错误");
        }
        req.getRequestDispatcher("/review.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        int orderId;
        int score;
        try {
            orderId = Integer.parseInt(req.getParameter("orderId"));
            score = Integer.parseInt(req.getParameter("score"));
        } catch (NumberFormatException e) {
            req.setAttribute("msg", "参数格式错误");
            req.getRequestDispatcher("/review.jsp").forward(req, resp);
            return;
        }
        String content = req.getParameter("content");
        try {
            // 评价成功：订单完成 + 自动结算
            reviewService.submit(loginUser, orderId, score, content);
            resp.sendRedirect(req.getContextPath() + "/order/list");
        } catch (Exception e) {
            req.setAttribute("msg", e.getMessage());
            try {
                req.setAttribute("order", orderService.findById(orderId));
            } catch (Exception ignored) { }
            req.getRequestDispatcher("/review.jsp").forward(req, resp);
        }
    }
}
