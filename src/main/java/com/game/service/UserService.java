package com.game.service;

import com.game.dao.UserDao;
import com.game.entity.User;
import com.game.util.MD5Util;

/**
 * 用户业务层：注册、登录
 */
public class UserService {

    private UserDao userDao = new UserDao();

    /**
     * 用户注册
     * @return 注册成功后的用户对象
     */
    public User register(String username, String password, String confirmPassword,
                         String nickname, String phone) throws Exception {
        // ---------- 基础校验 ----------
        if (isEmpty(username)) {
            throw new IllegalArgumentException("登录名不能为空");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("登录名长度需在 3~20 位之间");
        }
        if (isEmpty(password) || password.length() < 6) {
            throw new IllegalArgumentException("密码不能为空且至少 6 位");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("两次输入的密码不一致");
        }
        if (isEmpty(nickname)) {
            throw new IllegalArgumentException("昵称不能为空");
        }
        if (!isEmpty(phone) && !phone.matches("^1[3-9]\\d{9}$")) {
            throw new IllegalArgumentException("手机号格式不正确");
        }
        // ---------- 业务校验：登录名唯一 ----------
        if (userDao.findByUsername(username) != null) {
            throw new IllegalArgumentException("该登录名已被注册");
        }
        // ---------- 构造实体并入库 ----------
        User user = new User();
        user.setUsername(username);
        user.setPassword(MD5Util.md5(password)); // 密码加密存储
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setRole(0); // 默认普通用户
        userDao.insert(user);
        return user;
    }

    /**
     * 用户登录
     * @return 登录成功的用户对象
     */
    public User login(String username, String password) throws Exception {
        if (isEmpty(username) || isEmpty(password)) {
            throw new IllegalArgumentException("登录名和密码不能为空");
        }
        User user = userDao.findByUsername(username);
        // 用户不存在或密码错误统一提示，避免泄露账号是否存在
        if (user == null || !user.getPassword().equals(MD5Util.md5(password))) {
            throw new IllegalArgumentException("登录名或密码错误");
        }
        return user;
    }

    /** 判断字符串是否为空 */
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
