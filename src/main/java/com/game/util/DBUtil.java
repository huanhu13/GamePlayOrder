package com.game.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 数据库工具类：统一管理连接的获取与关闭（原生 JDBC）
 *
 * <p>连接参数不再写死在代码里，按以下优先级读取：</p>
 * <ol>
 *     <li>classpath 下的 {@code db.properties}（本地开发用，已被 .gitignore 排除）</li>
 *     <li>JVM 启动参数 {@code -Ddb.url=...}</li>
 *     <li>环境变量 {@code DB_URL / DB_USERNAME / DB_PASSWORD}</li>
 *     <li>本类中的默认值（密码无默认值，必须外部提供）</li>
 * </ol>
 */
public class DBUtil {

    /** 默认连接地址，可在 db.properties 或环境变量中覆盖 */
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/gameplayorder"
            + "?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties props = loadProperties();
        URL = resolve(props, "db.url", "DB_URL", DEFAULT_URL);
        USERNAME = resolve(props, "db.username", "DB_USERNAME", DEFAULT_USERNAME);
        PASSWORD = resolve(props, "db.password", "DB_PASSWORD", "");

        if (PASSWORD.isEmpty()) {
            throw new ExceptionInInitializerError(
                    "数据库密码未配置：请复制 src/main/resources/db.properties.example 为 db.properties 并填写 db.password，"
                    + "或设置环境变量 DB_PASSWORD 后重启应用。");
        }
        try {
            // 注册驱动（只需执行一次）
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL 驱动加载失败：" + e.getMessage());
        }
    }

    /** 读取 classpath 下的 db.properties，不存在则返回空配置 */
    private static Properties loadProperties() {
        Properties props = new Properties();
        InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
        if (in == null) {
            return props;
        }
        try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("读取 db.properties 失败：" + e.getMessage());
        }
        return props;
    }

    /** 按 properties -> JVM 系统属性 -> 环境变量 -> 默认值 的顺序取值 */
    private static String resolve(Properties props, String key, String envKey, String defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            value = System.getProperty(key);
        }
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(envKey);
        }
        return (value == null || value.trim().isEmpty()) ? defaultValue : value.trim();
    }

    /** 获取数据库连接 */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /** 释放资源（查询） */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) { }
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) { }
        try { if (conn != null) conn.close(); } catch (SQLException ignored) { }
    }

    /** 释放资源（增删改） */
    public static void close(Statement stmt, Connection conn) {
        close(null, stmt, conn);
    }
}
