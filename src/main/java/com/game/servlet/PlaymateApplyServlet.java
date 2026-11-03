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
 * 陪玩师申请控制器：GET 显示申请页，POST 提交申请
 */
@WebServlet("/playmate/apply")
public class PlaymateApplyServlet extends HttpServlet {

    private PlaymateService playmateService = new PlaymateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        // 已申请过则显示审核状态
        try {
            req.setAttribute("myPlaymate", playmateService.listByStatus(null).stream()
                    .filter(p -> p.getUserId().equals(loginUser.getId()))
                    .findFirst().orElse(null));
        } catch (Exception ignored) { }
        req.getRequestDispatcher("/apply.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        String gameName = req.getParameter("gameName");
        String area = req.getParameter("area");
        String price = req.getParameter("price");
        String intro = req.getParameter("intro");
        try {
            playmateService.apply(loginUser, gameName, area, price, intro);
            // 申请成功：提示并回到首页
            req.setAttribute("msg", "申请已提交，请等待管理员审核");
            req.getRequestDispatcher("/apply.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("msg", e.getMessage());
            req.getRequestDispatcher("/apply.jsp").forward(req, resp);
        }
    }
}
