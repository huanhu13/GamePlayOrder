<%-- 公共顶部导航（被各页面 include） --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="nav">
    <a class="brand" href="${pageContext.request.contextPath}/home">陪玩订单系统</a>
    <a href="${pageContext.request.contextPath}/home">首页</a>
    <c:if test="${not empty loginUser}">
        <a href="${pageContext.request.contextPath}/playmate/list">陪玩大厅</a>
        <a href="${pageContext.request.contextPath}/order/list">我的订单</a>
        <%-- 陪玩师/普通用户可申请入驻 --%>
        <c:if test="${loginUser.role == 0 || loginUser.role == 1}">
            <a href="${pageContext.request.contextPath}/playmate/apply">申请陪玩师</a>
        </c:if>
        <%-- 管理员菜单 --%>
        <c:if test="${loginUser.role == 2}">
            <a href="${pageContext.request.contextPath}/admin/playmate">陪玩师审核</a>
            <a href="${pageContext.request.contextPath}/admin/settlement">结算记录</a>
        </c:if>
    </c:if>
    <c:if test="${empty loginUser}">
        <a href="${pageContext.request.contextPath}/playmate/list">陪玩大厅</a>
    </c:if>
    <div class="right">
        <c:choose>
            <c:when test="${not empty loginUser}">
                <span>您好，${loginUser.nickname}
                    <c:if test="${loginUser.role == 1}">[陪玩师]</c:if>
                    <c:if test="${loginUser.role == 2}">[管理员]</c:if>
                </span>
                <a href="${pageContext.request.contextPath}/logout">退出</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login.jsp">登录</a>
                <a class="btn btn-sm" href="${pageContext.request.contextPath}/register">免费注册</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
