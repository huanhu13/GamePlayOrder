package com.game.dao;

import com.game.entity.Playmate;
import com.game.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 陪玩师数据访问层（原生 JDBC）
 */
public class PlaymateDao {

    /** 新增陪玩师申请，返回自增主键 */
    public int insert(Playmate p) throws SQLException {
        String sql = "INSERT INTO playmate (user_id, game_name, area, price, intro, status, avg_score) VALUES (?, ?, ?, ?, ?, 0, 0)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getGameName());
            ps.setString(3, p.getArea());
            ps.setBigDecimal(4, p.getPrice());
            ps.setString(5, p.getIntro());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    /** 根据主键查询 */
    public Playmate findById(int id) throws SQLException {
        String sql = "SELECT * FROM playmate WHERE id = ?";
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

    /** 根据关联用户 id 查询（判断是否已申请过） */
    public Playmate findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM playmate WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    /** 按状态查询列表（联表取昵称），status 传 null 查全部 */
    public List<Playmate> listByStatus(Integer status) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, u.nickname FROM playmate p JOIN user u ON p.user_id = u.id");
        if (status != null) {
            sql.append(" WHERE p.status = ?");
        }
        sql.append(" ORDER BY p.id DESC");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Playmate> list = new ArrayList<Playmate>();
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            if (status != null) {
                ps.setInt(1, status);
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

    /** 更新审核状态 */
    public void updateStatus(int id, int status) throws SQLException {
        String sql = "UPDATE playmate SET status = ? WHERE id = ?";
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

    /** 根据评价表重算陪玩师平均评分 */
    public void refreshAvgScore(int playmateId) throws SQLException {
        String sql = "UPDATE playmate SET avg_score = " +
                "(SELECT IFNULL(AVG(score), 0) FROM review WHERE playmate_id = ?) WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, playmateId);
            ps.setInt(2, playmateId);
            ps.executeUpdate();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /** 结果集 -> 实体映射 */
    private Playmate mapRow(ResultSet rs) throws SQLException {
        Playmate p = new Playmate();
        p.setId(rs.getInt("id"));
        p.setUserId(rs.getInt("user_id"));
        p.setGameName(rs.getString("game_name"));
        p.setArea(rs.getString("area"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setIntro(rs.getString("intro"));
        p.setStatus(rs.getInt("status"));
        p.setAvgScore(rs.getBigDecimal("avg_score"));
        try {
            // 联表查询时才有 nickname 字段
            p.setNickname(rs.getString("nickname"));
        } catch (SQLException ignored) { }
        return p;
    }
}
