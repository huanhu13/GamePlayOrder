package com.game.dao;

import com.game.entity.Settlement;
import com.game.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 结算数据访问层（原生 JDBC）
 */
public class SettlementDao {

    /** 新增结算记录 */
    public void insert(Settlement s) throws SQLException {
        String sql = "INSERT INTO settlement (order_id, total_money, platform_rate, playmate_income, settle_time) " +
                "VALUES (?, ?, ?, ?, NOW())";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, s.getOrderId());
            ps.setBigDecimal(2, s.getTotalMoney());
            ps.setBigDecimal(3, s.getPlatformRate());
            ps.setBigDecimal(4, s.getPlaymateIncome());
            ps.executeUpdate();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /** 查询全部结算记录（联表取订单号、陪玩师昵称） */
    public List<Settlement> listAll() throws SQLException {
        String sql = "SELECT s.*, o.order_no, pu.nickname AS playmate_nickname " +
                "FROM settlement s " +
                "JOIN orders o ON s.order_id = o.id " +
                "JOIN playmate p ON o.playmate_id = p.id " +
                "JOIN user pu ON p.user_id = pu.id " +
                "ORDER BY s.settle_time DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Settlement> list = new ArrayList<Settlement>();
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Settlement s = new Settlement();
                s.setId(rs.getInt("id"));
                s.setOrderId(rs.getInt("order_id"));
                s.setTotalMoney(rs.getBigDecimal("total_money"));
                s.setPlatformRate(rs.getBigDecimal("platform_rate"));
                s.setPlaymateIncome(rs.getBigDecimal("playmate_income"));
                s.setSettleTime(rs.getTimestamp("settle_time"));
                s.setOrderNo(rs.getString("order_no"));
                s.setPlaymateNickname(rs.getString("playmate_nickname"));
                list.add(s);
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }
}
