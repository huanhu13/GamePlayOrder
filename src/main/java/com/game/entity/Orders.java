package com.game.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类，对应 orders 表
 */
public class Orders {

    /** 主键 */
    private Integer id;
    /** 订单号 */
    private String orderNo;
    /** 下单用户 id */
    private Integer userId;
    /** 陪玩师 id */
    private Integer playmateId;
    /** 下单时间 */
    private Date orderTime;
    /** 预约时间 */
    private Date appointTime;
    /** 陪玩时长（小时） */
    private Integer hours;
    /** 总金额 */
    private BigDecimal totalMoney;
    /** 备注 */
    private String remark;
    /** 状态：0待接单 1已接单 2进行中 3待评价 4已完成 5已取消 */
    private Integer status;
    /** 完成时间 */
    private Date finishTime;

    /** 附加字段（联表查询用，非表字段） */
    private String userNickname;    // 下单用户昵称
    private String playmateNickname;// 陪玩师昵称
    private String gameName;        // 陪玩游戏
    private BigDecimal price;       // 陪玩单价

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getPlaymateId() { return playmateId; }
    public void setPlaymateId(Integer playmateId) { this.playmateId = playmateId; }
    public Date getOrderTime() { return orderTime; }
    public void setOrderTime(Date orderTime) { this.orderTime = orderTime; }
    public Date getAppointTime() { return appointTime; }
    public void setAppointTime(Date appointTime) { this.appointTime = appointTime; }
    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }
    public BigDecimal getTotalMoney() { return totalMoney; }
    public void setTotalMoney(BigDecimal totalMoney) { this.totalMoney = totalMoney; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getFinishTime() { return finishTime; }
    public void setFinishTime(Date finishTime) { this.finishTime = finishTime; }
    public String getUserNickname() { return userNickname; }
    public void setUserNickname(String userNickname) { this.userNickname = userNickname; }
    public String getPlaymateNickname() { return playmateNickname; }
    public void setPlaymateNickname(String playmateNickname) { this.playmateNickname = playmateNickname; }
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
