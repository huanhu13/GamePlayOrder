<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>注册 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrap">
    <div class="auth-img">
        <img src="${pageContext.request.contextPath}/images/arcade.jpg" alt="游戏氛围">
    </div>
    <div class="auth-form">
        <h2>创建账号 🚀</h2>
        <p class="sub">注册即送新人优惠券，首单立减 10 元</p>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-item">
                <label>登录名</label>
                <input type="text" name="username" value="${username}" required placeholder="3~20位字母或数字">
            </div>
            <div class="form-item">
                <label>密码</label>
                <input type="password" name="password" required placeholder="至少6位">
            </div>
            <div class="form-item">
                <label>确认密码</label>
                <input type="password" name="confirmPassword" required>
            </div>
            <div class="form-item">
                <label>昵称</label>
                <input type="text" name="nickname" required placeholder="游戏中大家怎么称呼你">
            </div>
            <div class="form-item">
                <label>手机号</label>
                <input type="text" name="phone" placeholder="选填，方便联系">
            </div>
            <div class="form-item">
                <label></label>
                <button type="submit" class="btn btn-lg">免费注册 🎮</button>
            </div>
            <div class="form-item">
                <label></label>
                <a href="${pageContext.request.contextPath}/login.jsp">已有账号？去登录 →</a>
            </div>
        </form>
    </div>
</div>
<div class="footer">陪玩订单系统 · 实力陪玩，快乐开黑</div>
</body>
</html>
