-- =============================================
-- 游戏陪玩订单评价系统 数据库初始化脚本 (MySQL 8)
-- =============================================
DROP DATABASE IF EXISTS gameplayorder;
CREATE DATABASE gameplayorder DEFAULT CHARACTER SET utf8mb4;
USE gameplayorder;

-- 用户表
CREATE TABLE user (
    id          INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(50)  NOT NULL UNIQUE   COMMENT '登录名',
    password    VARCHAR(64)  NOT NULL          COMMENT '密码(MD5)',
    nickname    VARCHAR(50)  NOT NULL          COMMENT '昵称',
    phone       VARCHAR(20)                    COMMENT '手机号',
    role        TINYINT      NOT NULL DEFAULT 0 COMMENT '角色:0普通用户 1陪玩师 2管理员',
    create_time DATETIME     NOT NULL          COMMENT '注册时间'
) COMMENT '用户表';

-- 陪玩师表
CREATE TABLE playmate (
    id        INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id   INT      NOT NULL UNIQUE COMMENT '关联用户id',
    game_name VARCHAR(50)  NOT NULL COMMENT '擅长游戏',
    area      VARCHAR(50)  NOT NULL COMMENT '大区/段位',
    price     DECIMAL(10,2) NOT NULL COMMENT '每小时价格',
    intro     VARCHAR(255)           COMMENT '个人简介',
    status    TINYINT  NOT NULL DEFAULT 0 COMMENT '状态:0待审核 1通过 2驳回',
    avg_score DECIMAL(3,1) NOT NULL DEFAULT 0 COMMENT '平均评分'
) COMMENT '陪玩师表';

-- 订单表
CREATE TABLE orders (
    id           INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_no     VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    user_id      INT NOT NULL COMMENT '下单用户id',
    playmate_id  INT NOT NULL COMMENT '陪玩师id',
    order_time   DATETIME NOT NULL COMMENT '下单时间',
    appoint_time DATETIME NOT NULL COMMENT '预约时间',
    hours        INT NOT NULL COMMENT '陪玩时长(小时)',
    total_money  DECIMAL(10,2) NOT NULL COMMENT '总金额',
    remark       VARCHAR(255) COMMENT '备注',
    status       TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0待接单 1已接单 2进行中 3待评价 4已完成 5已取消',
    finish_time  DATETIME COMMENT '完成时间'
) COMMENT '订单表';

-- 评价表
CREATE TABLE review (
    id          INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_id    INT NOT NULL UNIQUE COMMENT '订单id',
    user_id     INT NOT NULL COMMENT '评价用户id',
    playmate_id INT NOT NULL COMMENT '被评价陪玩师id',
    score       TINYINT NOT NULL COMMENT '评分1-5',
    content     VARCHAR(255) COMMENT '评价内容',
    create_time DATETIME NOT NULL COMMENT '评价时间'
) COMMENT '评价表';

-- 结算表
CREATE TABLE settlement (
    id              INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_id        INT NOT NULL UNIQUE COMMENT '订单id',
    total_money     DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    platform_rate   DECIMAL(4,2)  NOT NULL COMMENT '平台抽成比例(如0.20)',
    playmate_income DECIMAL(10,2) NOT NULL COMMENT '陪玩师收入',
    settle_time     DATETIME NOT NULL COMMENT '结算时间'
) COMMENT '结算表';

-- =============================================
-- 测试数据（密码均为 123456 的 MD5）
-- =============================================
INSERT INTO user (username, password, nickname, phone, role, create_time) VALUES
('admin',   'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '13800000000', 2, NOW()),
('zhangsan','e10adc3949ba59abbe56e057f20f883e', '张三',       '13800000001', 0, NOW()),
('lisi',    'e10adc3949ba59abbe56e057f20f883e', '李四(陪玩)',  '13800000002', 1, NOW());

INSERT INTO playmate (user_id, game_name, area, price, intro, status, avg_score) VALUES
(3, '王者荣耀', '微信区·王者50星', 30.00, '国服打野，带你上分', 1, 0);

-- =============================================
-- 演示用陪玩师账号（密码均为 123456）
-- =============================================
INSERT INTO user (username, password, nickname, phone, role, create_time) VALUES
('xiaolu',  'e10adc3949ba59abbe56e057f20f883e', '小鹿',   '13800000003', 1, NOW()),
('atai',    'e10adc3949ba59abbe56e057f20f883e', '阿泰',   '13800000004', 1, NOW()),
('laok',    'e10adc3949ba59abbe56e057f20f883e', '老K',    '13800000005', 1, NOW()),
('naitang', 'e10adc3949ba59abbe56e057f20f883e', '奶糖',   '13800000006', 1, NOW()),
('daoke',   'e10adc3949ba59abbe56e057f20f883e', '刀客',   '13800000007', 1, NOW());

INSERT INTO playmate (user_id, game_name, area, price, intro, status, avg_score) VALUES
(4, '王者荣耀', 'QQ区·巅峰2000分', 38.00, '主玩中单法师，五杀收割机，声音好听不骂人', 1, 4.9),
(5, '英雄联盟', '电一·大师段位',   45.00, '前青训队员，ADC专精，可教学意识与补刀', 1, 4.8),
(6, '和平精英', '微信区·战神',     32.00, '稳如老狗刚枪王，四排带飞，吃鸡率七成', 1, 4.9),
(7, '原神',     '全角色满命',      25.00, '深渊满星代打，圣遗物搭配规划，声音治愈', 1, 5.0),
(8, '永劫无间', '双排·宗师',       40.00, '妖刀姬天花板，连招教学，带你上宗师', 1, 4.7);

-- =============================================
-- 演示订单
-- =============================================
INSERT INTO orders (order_no, user_id, playmate_id, order_time, appoint_time, hours, total_money, remark, status, finish_time) VALUES
('GP20261103001', 2, 1, NOW(), DATE_ADD(NOW(), INTERVAL 2 HOUR), 2, 60.00, '想练打野节奏', 0, NULL),
('GP20261102002', 2, 4, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 20 HOUR), 3, 96.00, '四排吃鸡', 4, DATE_SUB(NOW(), INTERVAL 18 HOUR));

-- 已完成订单的结算记录（订单2）
INSERT INTO settlement (order_id, total_money, platform_rate, playmate_income, settle_time) VALUES
(2, 96.00, 0.20, 76.80, DATE_SUB(NOW(), INTERVAL 18 HOUR));
