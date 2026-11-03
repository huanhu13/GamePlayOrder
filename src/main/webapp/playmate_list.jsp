<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>陪玩大厅 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>
<div class="container">
    <div class="panel">
        <div class="section-head">
            <h2>陪玩大厅 <small>在线 ${empty playmateList ? 0 : fn:length(playmateList)} 位大神，随叫随到</small></h2>
            <span class="more">支持筛选 · 排序</span>
        </div>

        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>

        <%-- 筛选 + 排序 --%>
        <div class="toolbar">
            <div class="chips">
                <span class="chip active" data-filter="全部">全部</span>
                <span class="chip" data-filter="王者荣耀">王者荣耀</span>
                <span class="chip" data-filter="英雄联盟">英雄联盟</span>
                <span class="chip" data-filter="和平精英">和平精英</span>
                <span class="chip" data-filter="原神">原神</span>
                <span class="chip" data-filter="永劫无间">永劫无间</span>
            </div>
            <div class="sorts">
                <span class="sort-btn active" data-sort="default">综合</span>
                <span class="sort-btn" data-sort="price">价格 ↑</span>
                <span class="sort-btn" data-sort="score">评分 ↓</span>
            </div>
        </div>

        <div class="mates" id="mateGrid">
            <c:forEach var="p" items="${playmateList}">
                <%@ include file="/common/cover.jspf" %>
                <div class="mate-card"
                     data-game="${p.gameName}"
                     data-price="${p.price}"
                     data-score="${empty p.avgScore ? 0 : p.avgScore}">
                    <div class="cover">
                        <img src="${cover}" alt="${p.gameName}">
                        <span class="online"><i></i>在线接单</span>
                        <span class="cert">✓ 认证</span>
                        <span class="game-tag">${p.gameName}</span>
                    </div>
                    <div class="mate-body">
                        <div class="mate-head">
                            <div class="avatar">${fn:substring(p.nickname, 0, 1)}</div>
                            <div>
                                <div class="name-row">
                                    <span class="name">${p.nickname}</span>
                                    <span class="vip">V${p.id % 3 + 3}</span>
                                </div>
                                <div class="area">${p.area}</div>
                            </div>
                            <div style="margin-left:auto;">
                                <div class="wave"><i></i><i></i><i></i><i></i><i></i></div>
                            </div>
                        </div>
                        <div class="tags">
                            <span class="tag">${p.area}</span>
                            <span class="tag">秒接单</span>
                            <span class="tag">耐心教学</span>
                        </div>
                        <div class="mate-intro">${empty p.intro ? '服务态度好，技术过硬，带你轻松赢下每一局！' : p.intro}</div>
                        <div class="mate-foot">
                            <div>
                                <div class="price">¥${p.price}<small> /小时</small></div>
                                <div class="meta">接单 <b>${p.id * 37 + 128}</b> · ⭐ ${p.avgScore}</div>
                            </div>
                            <c:choose>
                                <c:when test="${loginUser.role == 0}">
                                    <a class="btn btn-sm" href="${pageContext.request.contextPath}/order/create?playmateId=${p.id}">立即下单</a>
                                </c:when>
                                <c:when test="${empty loginUser}">
                                    <a class="btn btn-sm btn-ghost" href="${pageContext.request.contextPath}/login.jsp">登录下单</a>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-gray">玩家可下单</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <%-- JS 筛选无结果提示 --%>
        <div id="emptyTip" style="display:none; text-align:center; color:#8f9ab5; padding:36px 0;">
            没有匹配的陪玩师，换个游戏试试 🎮
        </div>

        <c:if test="${empty playmateList}">
            <p style="text-align:center; color:#8f9ab5; padding:40px 0;">
                暂无审核通过的陪玩师，快去<a href="${pageContext.request.contextPath}/playmate/apply">申请入驻</a>吧！
            </p>
        </c:if>
    </div>
</div>
<div class="footer">陪玩订单系统 · 实力陪玩，快乐开黑</div>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
