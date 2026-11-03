package com.game.servlet;

import com.game.entity.Playmate;
import com.game.service.PlaymateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 首页控制器：加载评分最高的陪玩师，供首页"大神推荐 / 人气榜单"展示
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    /** 首页推荐位数量 */
    private static final int RECOMMEND_SIZE = 3;

    private PlaymateService playmateService = new PlaymateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Playmate> all = playmateService.listApproved();
            // 按平均分倒序，取前 N 位作为首页推荐（null 排在最后）
            all.sort(Comparator.comparing(Playmate::getAvgScore,
                    Comparator.nullsLast(Comparator.<BigDecimal>reverseOrder())));
            List<Playmate> recommend = new ArrayList<>();
            for (Playmate p : all) {
                if (recommend.size() >= RECOMMEND_SIZE) {
                    break;
                }
                recommend.add(p);
            }
            req.setAttribute("recommendList", recommend);
            req.setAttribute("playmateCount", all.size());
        } catch (Exception e) {
            req.setAttribute("msg", "数据加载失败：" + e.getMessage());
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
