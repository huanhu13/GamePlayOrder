package com.game.entity;

import java.util.Date;

/**
 * 评价实体类，对应 review 表
 */
public class Review {

    /** 主键 */
    private Integer id;
    /** 订单 id */
    private Integer orderId;
    /** 评价用户 id */
    private Integer userId;
    /** 被评价陪玩师 id */
    private Integer playmateId;
    /** 评分 1-5 */
    private Integer score;
    /** 评价内容 */
    private String content;
    /** 评价时间 */
    private Date createTime;

    /** 附加字段：订单号（联表查询用，非表字段） */
    private String orderNo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getPlaymateId() { return playmateId; }
    public void setPlaymateId(Integer playmateId) { this.playmateId = playmateId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
}
