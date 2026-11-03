package com.game.servlet;

import com.game.entity.User;
import com.game.service.SettlementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 结算记录控制器（管理员）：展示所有结算记录
 */
@WebServlet("/admin/settlement")
public class SettlementServlet extends HttpServlet {

    private SettlementService settlementService = new SettlementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        // 管理员权限校验
        if (loginUser.getRole() != 2) {
            req.setAttribute("msg", "无权访问，仅管理员可用");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }
        try {
            req.setAttribute("settlementList", settlementService.listAll());
        } catch (Exception e) {
            req.setAttribute("msg", "查询结算失败：" + e.getMessage());
        }
        req.getRequestDispatcher("/admin_settlement.jsp").forward(req, resp);
    }
}
