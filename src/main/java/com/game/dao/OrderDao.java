package com.game.dao;

import com.game.entity.Orders;
import com.game.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单数据访问层（原生 JDBC）
 */
public class OrderDao {

    /** 查询字段（联表获取用户昵称、陪玩师信息） */
    private static final String BASE_SELECT =
            "SELECT o.*, u.nickname AS user_nickname, pu.nickname AS playmate_nickname, " +
            "p.game_name, p.price " +
            "FROM orders o " +
            "JOIN user u ON o.user_id = u.id " +
            "JOIN playmate p ON o.playmate_id = p.id " +
            "JOIN user pu ON p.user_id = pu.id ";

    /** 新增订单 */
    public void insert(Orders o) throws SQLException {
        String sql = "INSERT INTO orders (order_no, user_id, playmate_id, order_time, appoint_time, " +
                "hours, total_money, remark, status) VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, o.getOrderNo());
            ps.setInt(2, o.getUserId());
            ps.setInt(3, o.getPlaymateId());
            ps.setTimestamp(4, new java.sql.Timestamp(o.getAppointTime().getTime()));
            ps.setInt(5, o.getHours());
            ps.setBigDecimal(6, o.getTotalMoney());
            ps.setString(7, o.getRemark());
            ps.executeUpdate();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /** 根据主键查询 */
    public Orders findById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE o.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    /** 查询某用户的所有订单（按下单时间倒序） */
    public List<Orders> listByUserId(int userId) throws SQLException {
        String sql = BASE_SELECT + "WHERE o.user_id = ? ORDER BY o.order_time DESC";
        return queryList(sql, userId);
    }

    /** 查询某陪玩师（按关联用户 id）接到的所有订单 */
    public List<Orders> listByPlaymateUserId(int playmateUserId) throws SQLException {
        String sql = BASE_SELECT + "WHERE p.user_id = ? ORDER BY o.order_time DESC";
        return queryList(sql, playmateUserId);
    }

    /** 查询全部订单（管理员用） */
    public List<Orders> listAll() throws SQLException {
        String sql = BASE_SELECT + "ORDER BY o.order_time DESC";
        return queryList(sql, null);
    }

    /** 更新订单状态（不含完成时间） */
    public void updateStatus(int id, int status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /** 陪玩师完成服务：状态改为待评价(3)并记录完成时间 */
    public void finish(int id) throws SQLException {
        String sql = "UPDATE orders SET status = 3, finish_time = NOW() WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /** 通用列表查询 */
    private List<Orders> queryList(String sql, Integer param) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Orders> list = new ArrayList<Orders>();
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            if (param != null) {
                ps.setInt(1, param);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    /** 结果集 -> 实体映射 */
    private Orders mapRow(ResultSet rs) throws SQLException {
        Orders o = new Orders();
        o.setId(rs.getInt("id"));
        o.setOrderNo(rs.getString("order_no"));
        o.setUserId(rs.getInt("user_id"));
        o.setPlaymateId(rs.getInt("playmate_id"));
        o.setOrderTime(rs.getTimestamp("order_time"));
        o.setAppointTime(rs.getTimestamp("appoint_time"));
        o.setHours(rs.getInt("hours"));
        o.setTotalMoney(rs.getBigDecimal("total_money"));
        o.setRemark(rs.getString("remark"));
        o.setStatus(rs.getInt("status"));
        o.setFinishTime(rs.getTimestamp("finish_time"));
        o.setUserNickname(rs.getString("user_nickname"));
        o.setPlaymateNickname(rs.getString("playmate_nickname"));
        o.setGameName(rs.getString("game_name"));
        o.setPrice(rs.getBigDecimal("price"));
        return o;
    }
}
