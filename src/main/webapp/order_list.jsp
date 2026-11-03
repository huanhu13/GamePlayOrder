<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>我的订单 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>
<div class="container">
    <div class="section-head" style="margin:6px 4px 16px;">
        <h2>我的订单 <small>共 ${empty orderList ? 0 : fn:length(orderList)} 笔</small></h2>
        <a class="more" href="${pageContext.request.contextPath}/playmate/list">继续下单 →</a>
    </div>

    <c:if test="${not empty msg}">
        <div class="msg">${msg}</div>
    </c:if>

    <c:forEach var="o" items="${orderList}">
        <%-- 进度节点数：0待接单 1已接单 2进行中 3待评价 4已完成 --%>
        <c:set var="step" value="${o.status == 0 ? 1 : (o.status == 1 ? 2 : (o.status == 2 ? 3 : 4))}"/>
        <div class="order-card">
            <div class="order-top">
                <div class="game-ico">🎮</div>
                <div>
                    <div class="title">${o.gameName} · ${o.playmateNickname}</div>
                    <div class="no">订单号 ${o.orderNo}</div>
                </div>
                <c:choose>
                    <c:when test="${o.status == 0}"><span class="badge badge-warn">待接单</span></c:when>
                    <c:when test="${o.status == 1}"><span class="badge badge-info">已接单</span></c:when>
                    <c:when test="${o.status == 2}"><span class="badge badge-purple">进行中</span></c:when>
                    <c:when test="${o.status == 3}"><span class="badge badge-star">待评价</span></c:when>
                    <c:when test="${o.status == 4}"><span class="badge badge-ok">已完成</span></c:when>
                    <c:otherwise><span class="badge badge-gray">已取消</span></c:otherwise>
                </c:choose>
            </div>

            <%-- 进度条（已取消订单不展示） --%>
            <c:if test="${o.status != 5}">
                <div class="track">
                    <div class="node ${step >= 1 ? 'done' : ''}"><i></i><span>待接单</span></div>
                    <div class="node ${step >= 2 ? 'done' : ''}"><i></i><span>已接单</span></div>
                    <div class="node ${step >= 3 ? 'done' : ''}"><i></i><span>进行中</span></div>
                    <div class="node ${step >= 4 ? 'done' : ''}"><i></i><span>已完成</span></div>
                </div>
            </c:if>

            <div class="order-info">
                <span>下单用户 <b>${o.userNickname}</b></span>
                <span>预约时间 <b><fmt:formatDate value="${o.appointTime}" pattern="MM-dd HH:mm"/></b></span>
                <span>时长 <b>${o.hours} 小时</b></span>
                <span>金额 <b class="money">¥${o.totalMoney}</b></span>
            </div>

            <c:if test="${not empty o.remark}">
                <div style="color:#8f9ab5; font-size:13px; margin-bottom:12px;">备注：${o.remark}</div>
            </c:if>

            <div class="order-foot">
                <span class="time">下单时间 <fmt:formatDate value="${o.orderTime}" pattern="yyyy-MM-dd HH:mm"/></span>

                <%-- 陪玩师操作：接单 / 开始 / 完成 --%>
                <c:if test="${loginUser.role == 1}">
                    <c:if test="${o.status == 0}">
                        <form method="post" action="${pageContext.request.contextPath}/order/action">
                            <input type="hidden" name="op" value="accept">
                            <input type="hidden" name="id" value="${o.id}">
                            <button class="btn btn-sm btn-success" type="submit">接单</button>
                        </form>
                    </c:if>
                    <c:if test="${o.status == 1}">
                        <form method="post" action="${pageContext.request.contextPath}/order/action">
                            <input type="hidden" name="op" value="start">
                            <input type="hidden" name="id" value="${o.id}">
                            <button class="btn btn-sm" type="submit">开始服务</button>
                        </form>
                    </c:if>
                    <c:if test="${o.status == 2}">
                        <form method="post" action="${pageContext.request.contextPath}/order/action">
                            <input type="hidden" name="op" value="finish">
                            <input type="hidden" name="id" value="${o.id}">
                            <button class="btn btn-sm btn-success" type="submit">完成服务</button>
                        </form>
                    </c:if>
                </c:if>

                <%-- 下单用户操作：取消 / 评价 --%>
                <c:if test="${loginUser.id == o.userId}">
                    <c:if test="${o.status == 0 || o.status == 1}">
                        <form method="post" action="${pageContext.request.contextPath}/order/action">
                            <input type="hidden" name="op" value="cancel">
                            <input type="hidden" name="id" value="${o.id}">
                            <button class="btn btn-sm btn-warning" type="submit"
                                    onclick="return confirm('确定取消该订单吗？')">取消订单</button>
                        </form>
                    </c:if>
                    <c:if test="${o.status == 3}">
                        <a class="btn btn-sm btn-success"
                           href="${pageContext.request.contextPath}/review/add?orderId=${o.id}">去评价</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </c:forEach>

    <c:if test="${empty orderList}">
        <div class="panel" style="text-align:center; padding:56px 20px;">
            <div style="font-size:46px; margin-bottom:12px;">🎧</div>
            <p style="color:#aeb9d4; margin-bottom:20px;">还没有订单，挑一位大神开启你的上分之旅吧！</p>
            <a class="btn btn-lg" href="${pageContext.request.contextPath}/playmate/list">去陪玩大厅</a>
        </div>
    </c:if>
</div>
<div class="footer">陪玩订单系统 · 实力陪玩，快乐开黑</div>
</body>
</html>
