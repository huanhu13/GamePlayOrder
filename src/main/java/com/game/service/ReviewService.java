package com.game.service;

import com.game.dao.OrderDao;
import com.game.dao.PlaymateDao;
import com.game.dao.ReviewDao;
import com.game.dao.SettlementDao;
import com.game.entity.Orders;
import com.game.entity.Review;
import com.game.entity.Settlement;
import com.game.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 评价业务层：用户评价 + 完成订单 + 生成结算 + 刷新陪玩师评分
 */
public class ReviewService {

    /** 平台抽成比例（20%），可按需调整 */
    private static final BigDecimal PLATFORM_RATE = new BigDecimal("0.20");

    private ReviewDao reviewDao = new ReviewDao();
    private OrderDao orderDao = new OrderDao();
    private PlaymateDao playmateDao = new PlaymateDao();
    private SettlementDao settlementDao = new SettlementDao();

    /**
     * 用户提交评价：
     * 1. 校验订单归属与状态（3待评价）
     * 2. 保存评价记录
     * 3. 订单状态 -> 4已完成
     * 4. 刷新陪玩师平均评分
     * 5. 生成结算记录
     */
    public void submit(User loginUser, int orderId, int score, String content) throws Exception {
        // ---------- 订单校验 ----------
        Orders order = orderDao.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        if (!order.getUserId().equals(loginUser.getId())) {
            throw new IllegalArgumentException("只有下单用户本人才能评价");
        }
        if (order.getStatus() != 3) {
            throw new IllegalArgumentException("当前订单状态不可评价");
        }
        // ---------- 评分校验 ----------
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("评分必须是 1~5 的整数");
        }
        if (reviewDao.existsByOrderId(orderId)) {
            throw new IllegalArgumentException("该订单已评价过，请勿重复评价");
        }
        // ---------- 1. 保存评价 ----------
        Review review = new Review();
        review.setOrderId(orderId);
        review.setUserId(loginUser.getId());
        review.setPlaymateId(order.getPlaymateId());
        review.setScore(score);
        review.setContent(content);
        reviewDao.insert(review);
        // ---------- 2. 订单改为已完成 ----------
        orderDao.updateStatus(orderId, 4);
        // ---------- 3. 刷新陪玩师平均评分 ----------
        playmateDao.refreshAvgScore(order.getPlaymateId());
        // ---------- 4. 生成结算：收入 = 总金额 x (1 - 平台抽成) ----------
        Settlement settlement = new Settlement();
        settlement.setOrderId(orderId);
        settlement.setTotalMoney(order.getTotalMoney());
        settlement.setPlatformRate(PLATFORM_RATE);
        settlement.setPlaymateIncome(order.getTotalMoney()
                .multiply(BigDecimal.ONE.subtract(PLATFORM_RATE))
                .setScale(2, RoundingMode.HALF_UP)); // 保留两位小数，四舍五入
        settlementDao.insert(settlement);
    }
}
