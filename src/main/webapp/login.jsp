<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrap">
    <div class="auth-img">
        <img src="${pageContext.request.contextPath}/images/esports-team.jpg" alt="电竞战队">
    </div>
    <div class="auth-form">
        <h2>欢迎回来 👋</h2>
        <p class="sub">登录后挑选大神陪玩，即刻开黑上分</p>
        <%-- 登录过滤器跳转时提示 --%>
        <c:if test="${not empty sessionScope.msg}">
            <div class="msg">${sessionScope.msg}</div>
            <c:remove var="msg" scope="session"/>
        </c:if>
        <%-- Servlet 转发回来的错误提示 --%>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-item">
                <label>登录名</label>
                <input type="text" name="username" value="${username}" required placeholder="请输入登录名">
            </div>
            <div class="form-item">
                <label>密码</label>
                <input type="password" name="password" required placeholder="请输入密码">
            </div>
            <div class="form-item">
                <label></label>
                <button type="submit" class="btn btn-lg">登 录 🎮</button>
            </div>
            <div class="form-item">
                <label></label>
                <a href="${pageContext.request.contextPath}/register">没有账号？立即注册 →</a>
            </div>
        </form>
    </div>
</div>
<div class="footer">陪玩订单系统 · 实力陪玩，快乐开黑</div>
</body>
</html>
