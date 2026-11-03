package com.game.dao;

import com.game.entity.Review;
import com.game.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 评价数据访问层（原生 JDBC）
 */
public class ReviewDao {

    /** 新增评价 */
    public void insert(Review r) throws SQLException {
        String sql = "INSERT INTO review (order_id, user_id, playmate_id, score, content, create_time) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, r.getOrderId());
            ps.setInt(2, r.getUserId());
            ps.setInt(3, r.getPlaymateId());
            ps.setInt(4, r.getScore());
            ps.setString(5, r.getContent());
            ps.executeUpdate();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /** 判断某订单是否已评价过（防止重复评价） */
    public boolean existsByOrderId(int orderId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM review WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }
}
