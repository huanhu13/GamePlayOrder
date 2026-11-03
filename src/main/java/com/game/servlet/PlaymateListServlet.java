package com.game.servlet;

import com.game.service.PlaymateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 陪玩师列表控制器：展示审核通过的陪玩师供用户浏览下单
 */
@WebServlet("/playmate/list")
public class PlaymateListServlet extends HttpServlet {

    private PlaymateService playmateService = new PlaymateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("playmateList", playmateService.listApproved());
        } catch (Exception e) {
            req.setAttribute("msg", "查询陪玩师失败：" + e.getMessage());
        }
        req.getRequestDispatcher("/playmate_list.jsp").forward(req, resp);
    }
}
