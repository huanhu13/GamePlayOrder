package com.game.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算实体类，对应 settlement 表
 */
public class Settlement {

    /** 主键 */
    private Integer id;
    /** 订单 id */
    private Integer orderId;
    /** 订单总金额 */
    private BigDecimal totalMoney;
    /** 平台抽成比例（如 0.20 表示 20%） */
    private BigDecimal platformRate;
    /** 陪玩师收入 */
    private BigDecimal playmateIncome;
    /** 结算时间 */
    private Date settleTime;

    /** 附加字段（联表查询用，非表字段） */
    private String orderNo;         // 订单号
    private String playmateNickname;// 陪玩师昵称

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public BigDecimal getTotalMoney() { return totalMoney; }
    public void setTotalMoney(BigDecimal totalMoney) { this.totalMoney = totalMoney; }
    public BigDecimal getPlatformRate() { return platformRate; }
    public void setPlatformRate(BigDecimal platformRate) { this.platformRate = platformRate; }
    public BigDecimal getPlaymateIncome() { return playmateIncome; }
    public void setPlaymateIncome(BigDecimal playmateIncome) { this.playmateIncome = playmateIncome; }
    public Date getSettleTime() { return settleTime; }
    public void setSettleTime(Date settleTime) { this.settleTime = settleTime; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getPlaymateNickname() { return playmateNickname; }
    public void setPlaymateNickname(String playmateNickname) { this.playmateNickname = playmateNickname; }
}
