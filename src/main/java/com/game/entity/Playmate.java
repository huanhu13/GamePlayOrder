package com.game.entity;

import java.math.BigDecimal;

/**
 * 陪玩师实体类，对应 playmate 表
 */
public class Playmate {

    /** 主键 */
    private Integer id;
    /** 关联用户 id */
    private Integer userId;
    /** 擅长游戏 */
    private String gameName;
    /** 大区/段位 */
    private String area;
    /** 每小时价格 */
    private BigDecimal price;
    /** 个人简介 */
    private String intro;
    /** 状态：0待审核 1通过 2驳回 */
    private Integer status;
    /** 平均评分 */
    private BigDecimal avgScore;

    /** 附加字段：关联用户昵称（联表查询用，非表字段） */
    private String nickname;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public BigDecimal getAvgScore() { return avgScore; }
    public void setAvgScore(BigDecimal avgScore) { this.avgScore = avgScore; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
