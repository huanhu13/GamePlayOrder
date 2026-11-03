# GamePlayOrder · 游戏陪玩订单评价系统

基于 **Servlet + JSP + JDBC + MySQL** 的 JavaWeb 课程设计项目，实现「用户下单 → 陪玩师接单 → 完成服务 → 评价 → 平台结算」的完整业务闭环，附带深色电竞风前端界面。

## 技术栈

| 层次 | 技术 |
|---|---|
| 表现层 | JSP + JSTL + 原生 CSS（深色电竞风） |
| 控制层 | Servlet 4.0 + Filter（编码 / 登录校验） |
| 业务层 | Service（手写事务与业务规则） |
| 持久层 | 原生 JDBC + DAO 模式 |
| 数据库 | MySQL 8 |
| 构建 | Maven（war 包） |
| 服务器 | Tomcat 9（`javax.servlet`，**不兼容 Tomcat 10+**） |

## 功能一览

**普通用户（role=0）**
- 注册 / 登录 / 退出
- 浏览陪玩大厅（封面卡片、评分、价格）
- 创建订单：预约时间、时长、备注，金额按 `价格 × 时长` 自动计算
- 查看订单并跟踪状态、取消订单
- 完成服务后打星评价（自动回写陪玩师平均分）
- 申请成为陪玩师

**陪玩师（role=1）**
- 接单 / 开始服务 / 完成服务
- 查看自己的订单与收入（平台抽成 20%）

**管理员（role=2）**
- 陪玩师入驻审核（通过 / 驳回）
- 结算记录查看（订单金额、抽成比例、平台收入、陪玩师收入）

**订单状态机**

```
0 待接单 → 1 已接单 → 2 进行中 → 3 待评价 → 4 已完成
                ↘ 5 已取消
```

## 目录结构

```
GamePlayOrder/
├─ pom.xml
├─ sql/gameplayorder.sql          # 建库建表 + 演示数据
├─ src/main/
│  ├─ java/com/game/
│  │  ├─ entity/                  # User、Playmate、Orders、Review、Settlement
│  │  ├─ dao/                     # 5 个 DAO，纯 JDBC
│  │  ├─ service/                 # 业务逻辑与事务控制
│  │  ├─ servlet/                 # 11 个 Servlet（@WebServlet 注解注册）
│  │  ├─ filter/                  # EncodingFilter、LoginFilter
│  │  └─ util/                    # DBUtil、MD5Util
│  ├─ resources/
│  │  ├─ db.properties            # 本地数据库配置（不提交）
│  │  └─ db.properties.example    # 配置模板
│  └─ webapp/
│     ├─ *.jsp                    # 12 个页面
│     ├─ common/nav.jsp           # 公共导航
│     ├─ css/style.css
│     ├─ images/                  # 电竞配图（Unsplash 免费可商用 + AI 生成）
│     └─ WEB-INF/web.xml
```

## 快速开始

### 1. 环境要求

- JDK 8 及以上（推荐 17）
- MySQL 8.0
- Tomcat 9.x
- Maven 3.6+（IDEA 内置 Maven 亦可）

### 2. 初始化数据库

```bash
mysql -u root -p --default-character-set=utf8mb4 -e "source sql/gameplayorder.sql"
```

脚本会创建 `gameplayorder` 库、5 张表和演示数据（含 6 位陪玩师、示例订单与结算记录）。

> Windows 下若路径含中文导致 `mysql` 报 `Failed to open file`，先把脚本复制到纯英文路径再 `source`。

### 3. 配置数据库连接

```bash
# 从模板复制一份本地配置，填写自己的密码
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

`db.properties` 含明文密码，已在 `.gitignore` 中排除，**不会上传到仓库**。

也支持不改文件的覆盖方式（优先级从高到低）：`db.properties` → JVM 参数 `-Ddb.password=xxx` → 环境变量 `DB_PASSWORD`。

### 4. 打包与部署

```bash
mvn clean package
cp target/GamePlayOrder.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh      # Windows: startup.bat
```

### 5. 访问

```
http://localhost:8080/GamePlayOrder/
```

## 测试账号

| 账号 | 密码 | 角色 |
|---|---|---|
| admin | 123456 | 管理员 |
| zhangsan | 123456 | 普通用户 |
| lisi / xiaolu / atai / laok / naitang / daoke | 123456 | 陪玩师 |

## 数据表

| 表 | 说明 |
|---|---|
| `user` | 用户，`role` 区分 0 普通 / 1 陪玩师 / 2 管理员，密码 MD5 存储 |
| `playmate` | 陪玩师资料，`status` 0 待审核 / 1 通过 / 2 驳回，`avg_score` 平均分 |
| `orders` | 订单，`status` 0~5 对应状态机 |
| `review` | 评价，`order_id` 唯一，一单一评 |
| `settlement` | 结算，`platform_rate` 平台抽成比例 |

## 常见问题

**Q：部署到 Tomcat 10/11 后 404 或 `ClassNotFoundException: javax.servlet.*`？**
A：项目基于 Servlet 4.0（`javax.*` 包名），Tomcat 10 起改用 `jakarta.*`，必须使用 **Tomcat 9**。

**Q：启动报「数据库密码未配置」？**
A：未提供 `db.properties`。复制模板并填写 `db.password`，或设置环境变量 `DB_PASSWORD`。

**Q：页面中文乱码？**
A：数据库连接串已带 `characterEncoding=utf8`，导入脚本时请加 `--default-character-set=utf8mb4`。

## 图片来源

- [Unsplash](https://unsplash.com)（免费可商用）电竞主题摄影
- AI 生成的 MOBA 风格封面（`images/game-moba.png`）

## 说明

本项目为课程设计用途，未接入真实支付与实名认证，订单金额、结算比例均为演示数据。
