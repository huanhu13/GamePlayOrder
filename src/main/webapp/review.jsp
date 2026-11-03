<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>订单评价 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>
<div class="container" style="max-width:760px;">
    <div class="panel">
        <h2>给这次开黑打个分吧</h2>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <c:choose>
            <c:when test="${empty order}">
                <p style="padding:20px 0; color:#aeb9d4;">订单不存在，请返回 <a href="${pageContext.request.contextPath}/order/list">订单列表</a>。</p>
            </c:when>
            <c:otherwise>
                <p style="margin-bottom:20px; color:#aeb9d4; line-height:1.9;">
                    订单号：<b style="color:#fff;">${order.orderNo}</b> ｜
                    陪玩师：<b style="color:#fff;">${order.playmateNickname}</b>（${order.gameName}）｜
                    实付：<b style="color:#ffd166;">¥${order.totalMoney}</b>
                </p>
                <form action="${pageContext.request.contextPath}/review/add" method="post">
                    <input type="hidden" name="orderId" value="${order.id}">
                    <div class="form-item">
                        <label>综合评分</label>
                        <%-- DOM 从 5 到 1 排列，配合 CSS row-reverse 呈现为 5★ 在左 --%>
                        <span class="star-row">
                            <label title="非常满意"><input type="radio" name="score" value="5" checked>★</label>
                            <label title="满意"><input type="radio" name="score" value="4">★</label>
                            <label title="一般"><input type="radio" name="score" value="3">★</label>
                            <label title="不满意"><input type="radio" name="score" value="2">★</label>
                            <label title="很差"><input type="radio" name="score" value="1">★</label>
                        </span>
                    </div>
                    <div class="form-item">
                        <label>评价内容</label>
                        <textarea name="content" rows="4" cols="40" placeholder="说说这次开黑体验吧：技术如何？态度怎么样？">大神技术过硬，全程氛围拉满，下次还找TA！</textarea>
                    </div>
                    <div class="form-item">
                        <label></label>
                        <button type="submit" class="btn btn-lg">提交评价 ⭐</button>
                        <a href="${pageContext.request.contextPath}/order/list">返回订单列表</a>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div class="footer">陪玩订单系统 · 实力陪玩，快乐开黑</div>
</body>
</html>
