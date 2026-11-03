package com.game.dao;

import com.game.entity.User;
import com.game.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 用户数据访问层（原生 JDBC）
 */
public class UserDao {

    /** 根据登录名查询用户（登录、注册查重用） */
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    /** 根据主键查询用户 */
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
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

    /** 新增用户，返回自增主键 */
    public int insert(User user) throws SQLException {
        String sql = "INSERT INTO user (username, password, nickname, phone, role, create_time) VALUES (?, ?, ?, ?, ?, NOW())";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickname());
            ps.setString(4, user.getPhone());
            ps.setInt(5, user.getRole() == null ? 0 : user.getRole());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    /** 更新用户角色（陪玩师审核通过时使用） */
    public void updateRole(int userId, int role) throws SQLException {
        String sql = "UPDATE user SET role = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, role);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /** 结果集 -> 实体映射 */
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getInt("role"));
        user.setCreateTime(rs.getTimestamp("create_time"));
        return user;
    }
}
