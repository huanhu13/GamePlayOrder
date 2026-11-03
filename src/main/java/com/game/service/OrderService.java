package com.game.service;

import com.game.dao.OrderDao;
import com.game.dao.PlaymateDao;
import com.game.entity.Orders;
import com.game.entity.Playmate;
import com.game.entity.User;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 订单业务层：下单、接单、开始、完成、取消，以及订单状态流转控制
 *
 * 状态流转：
 * 0待接单 --陪玩师接单--> 1已接单 --陪玩师开始--> 2进行中
 *         --陪玩师完成--> 3待评价 --用户评价后(ReviewService)--> 4已完成
 * 0/1 状态可由下单用户取消 --> 5已取消
 */
public class OrderService {

    private OrderDao orderDao = new OrderDao();
    private PlaymateDao playmateDao = new PlaymateDao();

    /**
     * 用户创建订单
     */
    public void create(User loginUser, int playmateId, String appointTimeStr,
                       String hoursStr, String remark) throws Exception {
        // ---------- 基础校验 ----------
        Playmate playmate = playmateDao.findById(playmateId);
        if (playmate == null) {
            throw new IllegalArgumentException("陪玩师不存在");
        }
        if (playmate.getStatus() != 1) {
            throw new IllegalArgumentException("该陪玩师未通过审核，无法下单");
        }
        if (playmate.getUserId() == loginUser.getId()) {
            throw new IllegalArgumentException("不能给自己下单");
        }
        Date appointTime;
        try {
            appointTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(appointTimeStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("预约时间格式不正确");
        }
        if (appointTime.before(new Date())) {
            throw new IllegalArgumentException("预约时间不能早于当前时间");
        }
        int hours;
        try {
            hours = Integer.parseInt(hoursStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("陪玩时长必须是整数");
        }
        if (hours < 1 || hours > 12) {
            throw new IllegalArgumentException("陪玩时长需在 1~12 小时之间");
        }
        // ---------- 计算总金额：单价 x 时长 ----------
        BigDecimal totalMoney = playmate.getPrice().multiply(new BigDecimal(hours));
        // ---------- 构造订单并入库 ----------
        Orders order = new Orders();
        order.setOrderNo(generateOrderNo());
        order.setUserId(loginUser.getId());
        order.setPlaymateId(playmateId);
        order.setAppointTime(appointTime);
        order.setHours(hours);
        order.setTotalMoney(totalMoney);
        order.setRemark(remark);
        order.setStatus(0); // 待接单
        orderDao.insert(order);
    }

    /**
     * 陪玩师接单：0待接单 -> 1已接单
     */
    public void accept(User loginUser, int orderId) throws Exception {
        Orders order = checkAndGet(loginUser, orderId);
        if (order.getStatus() != 0) {
            throw new IllegalArgumentException("当前订单状态不可接单");
        }
        orderDao.updateStatus(orderId, 1);
    }

    /**
     * 陪玩师开始服务：1已接单 -> 2进行中
     */
    public void start(User loginUser, int orderId) throws Exception {
        Orders order = checkAndGet(loginUser, orderId);
        if (order.getStatus() != 1) {
            throw new IllegalArgumentException("当前订单状态不可开始服务");
        }
        orderDao.updateStatus(orderId, 2);
    }

    /**
     * 陪玩师完成服务：2进行中 -> 3待评价，并记录完成时间
     */
    public void finish(User loginUser, int orderId) throws Exception {
        Orders order = checkAndGet(loginUser, orderId);
        if (order.getStatus() != 2) {
            throw new IllegalArgumentException("当前订单状态不可完成服务");
        }
        orderDao.finish(orderId);
    }

    /**
     * 取消订单：0待接单/1已接单 状态下，下单用户可取消 -> 5已取消
     */
    public void cancel(User loginUser, int orderId) throws Exception {
        Orders order = orderDao.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        // 只有下单用户本人可以取消
        if (!order.getUserId().equals(loginUser.getId())) {
            throw new IllegalArgumentException("只有下单用户本人才能取消订单");
        }
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new IllegalArgumentException("订单已开始服务，无法取消");
        }
        orderDao.updateStatus(orderId, 5);
    }

    /**
     * 按登录用户角色查询订单列表：
     * 普通用户 -> 我下单的；陪玩师 -> 我接的单；管理员 -> 全部订单
     */
    public List<Orders> listForUser(User loginUser) throws Exception {
        if (loginUser.getRole() == 2) {
            return orderDao.listAll();
        }
        if (loginUser.getRole() == 1) {
            return orderDao.listByPlaymateUserId(loginUser.getId());
        }
        return orderDao.listByUserId(loginUser.getId());
    }

    /** 根据主键查询订单（详情/评价页用） */
    public Orders findById(int orderId) throws Exception {
        return orderDao.findById(orderId);
    }

    /**
     * 校验订单存在且属于当前陪玩师，返回订单对象
     */
    private Orders checkAndGet(User loginUser, int orderId) throws Exception {
        Orders order = orderDao.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }
        Playmate playmate = playmateDao.findById(order.getPlaymateId());
        if (playmate == null || !playmate.getUserId().equals(loginUser.getId())) {
            throw new IllegalArgumentException("无权操作他人订单");
        }
        return order;
    }

    /** 生成唯一订单号：GP + 时间戳 + 3位随机数 */
    private String generateOrderNo() {
        return "GP" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + String.format("%03d", new Random().nextInt(1000));
    }
}
