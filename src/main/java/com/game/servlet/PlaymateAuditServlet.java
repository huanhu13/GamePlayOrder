package com.game.servlet;

import com.game.entity.User;
import com.game.service.PlaymateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 陪玩师审核控制器（管理员）：GET 显示待审核列表，POST 执行审核
 */
@WebServlet("/admin/playmate")
public class PlaymateAuditServlet extends HttpServlet {

    private PlaymateService playmateService = new PlaymateService();

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
            // 展示全部申请，便于查看已审核记录
            req.setAttribute("playmateList", playmateService.listByStatus(null));
        } catch (Exception e) {
            req.setAttribute("msg", "查询失败：" + e.getMessage());
        }
        req.getRequestDispatcher("/admin_playmate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        String op = req.getParameter("op");
        try {
            int playmateId = Integer.parseInt(req.getParameter("id"));
            // op=pass 通过，op=reject 驳回
            playmateService.audit(loginUser, playmateId, "pass".equals(op));
        } catch (NumberFormatException e) {
            req.setAttribute("msg", "参数错误");
        } catch (Exception e) {
            req.setAttribute("msg", e.getMessage());
        }
        // 审核后重新展示列表
        try {
            req.setAttribute("playmateList", playmateService.listByStatus(null));
        } catch (Exception ignored) { }
        req.getRequestDispatcher("/admin_playmate.jsp").forward(req, resp);
    }
}
