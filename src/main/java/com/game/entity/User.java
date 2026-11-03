package com.game.entity;

import java.util.Date;

/**
 * 用户实体类，对应 user 表
 */
public class User {

    /** 主键 */
    private Integer id;
    /** 登录名 */
    private String username;
    /** 密码（MD5） */
    private String password;
    /** 昵称 */
    private String nickname;
    /** 手机号 */
    private String phone;
    /** 角色：0普通用户 1陪玩师 2管理员 */
    private Integer role;
    /** 注册时间 */
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
