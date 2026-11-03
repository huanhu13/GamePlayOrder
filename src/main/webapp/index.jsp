<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>首页 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>

<div class="container">

    <%-- ===== 搜索栏 ===== --%>
    <form class="searchbar" action="${pageContext.request.contextPath}/playmate/list" method="get">
        <span class="icon">🔍</span>
        <input type="text" name="keyword" placeholder="搜索游戏 / 陪玩师，如：王者荣耀、吃鸡、上分">
        <div class="hotwords">
            <span>王者荣耀</span><span>和平精英</span><span>大神带飞</span>
        </div>
        <button class="btn btn-sm" type="submit">搜索</button>
    </form>

    <%-- ===== 轮播 Banner ===== --%>
    <div class="banner">
        <div class="slide active" style="background-image:url('${pageContext.request.contextPath}/images/hero.jpg')"></div>
        <div class="slide" style="background-image:url('${pageContext.request.contextPath}/images/game-moba.png')"></div>
        <div class="slide" style="background-image:url('${pageContext.request.contextPath}/images/esports-stage.jpg')"></div>

        <div class="banner-text">
            <span class="hero-tag">PRO · ESPORTS COMPANION</span>
            <h1>上分开黑<br>就差一个<em>大神队友</em></h1>
            <p>真人认证陪玩在线接单，全游戏覆盖 · 一键预约 · 安全支付 · 完单评价</p>
            <c:choose>
                <c:when test="${empty loginUser}">
                    <a class="btn btn-lg" href="${pageContext.request.contextPath}/register">立即注册，免费开黑</a>
                    <a class="btn btn-lg btn-ghost" href="${pageContext.request.contextPath}/login.jsp">已有账号登录</a>
                </c:when>
                <c:when test="${loginUser.role == 0}">
                    <a class="btn btn-lg" href="${pageContext.request.contextPath}/playmate/list">进入陪玩大厅</a>
                    <a class="btn btn-lg btn-ghost" href="${pageContext.request.contextPath}/order/list">我的订单</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-lg" href="${pageContext.request.contextPath}/order/list">进入工作台</a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="dots">
            <i class="active"></i><i></i><i></i>
        </div>
    </div>

    <%-- ===== 游戏分类 ===== --%>
    <div class="panel" style="padding:20px 22px;">
        <div class="cat-grid">
            <a class="cat" href="${pageContext.request.contextPath}/playmate/list">
                <span class="ico">⚔️</span><b>王者荣耀</b><span>上分带飞</span>
            </a>
            <a class="cat" href="${pageContext.request.contextPath}/playmate/list">
                <span class="ico">🛡️</span><b>英雄联盟</b><span>双排上星</span>
            </a>
            <a class="cat" href="${pageContext.request.contextPath}/playmate/list">
                <span class="ico">🎯</span><b>和平精英</b><span>刚枪吃鸡</span>
            </a>
            <a class="cat" href="${pageContext.request.contextPath}/playmate/list">
                <span class="ico">✨</span><b>原神</b><span>深渊代打</span>
            </a>
            <a class="cat" href="${pageContext.request.contextPath}/playmate/list">
                <span class="ico">🗡️</span><b>永劫无间</b><span>连招教学</span>
            </a>
            <a class="cat" href="${pageContext.request.contextPath}/playmate/list">
                <span class="ico">🎲</span><b>更多游戏</b><span>持续上新</span>
            </a>
        </div>
    </div>

    <c:if test="${not empty sessionScope.msg}">
        <div class="msg">${sessionScope.msg}</div>
        <c:remove var="msg" scope="session"/>
    </c:if>

    <%-- ===== 大神推荐 ===== --%>
    <c:if test="${not empty recommendList}">
        <div class="section-head">
            <h2>大神推荐 <small>评分最高的 3 位陪玩师</small></h2>
            <a class="more" href="${pageContext.request.contextPath}/playmate/list">查看全部 →</a>
        </div>
        <div class="mates" style="margin-bottom:24px;">
            <c:forEach var="p" items="${recommendList}">
                <%@ include file="/common/cover.jspf" %>
                <div class="mate-card">
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
                            <span class="tag">技术过硬</span>
                            <span class="tag">声音好听</span>
                            <span class="tag">耐心教学</span>
                        </div>
                        <div class="mate-intro">${empty p.intro ? '服务态度好，技术过硬，带你轻松赢下每一局！' : p.intro}</div>
                        <div class="mate-foot">
                            <div class="price">¥${p.price}<small> /小时</small></div>
                            <div class="meta">接单 <b>${p.id * 37 + 128}</b> · ⭐ ${p.avgScore}</div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <%-- ===== 人气榜 + 平台数据 ===== --%>
    <div style="display:grid; grid-template-columns: 1.4fr 1fr; gap:18px; margin-bottom:24px;">
        <div class="panel" style="margin-bottom:0;">
            <div class="section-head"><h2>本周人气榜</h2></div>
            <div class="rank-list">
                <c:forEach var="p" items="${recommendList}" varStatus="st">
                    <div class="rank-item">
                        <span class="rank-no">${st.index + 1}</span>
                        <div>
                            <div class="rank-name">${p.nickname}</div>
                            <div class="rank-sub">${p.gameName} · ${p.area}</div>
                        </div>
                        <span class="rank-score">⭐ ${p.avgScore}</span>
                    </div>
                </c:forEach>
                <c:if test="${empty recommendList}">
                    <div class="rank-item">
                        <span class="rank-no">1</span>
                        <div><div class="rank-name">暂无上榜陪玩师</div><div class="rank-sub">等待新大神入驻</div></div>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="panel" style="margin-bottom:0;">
            <div class="section-head"><h2>平台数据</h2></div>
            <div class="stats" style="flex-direction:column; gap:10px; margin:0;">
                <div class="stat" style="padding:14px;"><b>${empty playmateCount ? 0 : playmateCount}</b><span>在架陪玩师</span></div>
                <div class="stat" style="padding:14px;"><b>98.6%</b><span>完单好评率</span></div>
                <div class="stat" style="padding:14px;"><b>7×24h</b><span>全天在线接单</span></div>
            </div>
        </div>
    </div>

    <%-- ===== 平台优势 ===== --%>
    <div class="features">
        <div class="feature">
            <div class="icon">✅</div>
            <h3>真人认证 · 实名入驻</h3>
            <p>每一位陪玩师都经过平台资质审核，<br>段位可查、实力真实，拒绝照骗。</p>
        </div>
        <div class="feature">
            <div class="icon">🔒</div>
            <h3>平台担保 · 安全支付</h3>
            <p>订单资金由平台托管，服务完成<br>确认后再结算，下单更放心。</p>
        </div>
        <div class="feature">
            <div class="icon">⭐</div>
            <h3>评价透明 · 好评可查</h3>
            <p>每单完成后双向评价，评分实时<br>公开透明，好口碑一目了然。</p>
        </div>
    </div>

    <%-- ===== 使用流程 ===== --%>
    <div class="panel">
        <div class="section-head"><h2>四步开启上分之旅</h2></div>
        <div class="steps">
            <div class="step"><span class="num">1</span><p>注册 / 登录账号</p></div>
            <div class="step"><span class="num">2</span><p>挑选心仪陪玩师</p></div>
            <div class="step"><span class="num">3</span><p>预约下单 · 等待接单</p></div>
            <div class="step"><span class="num">4</span><p>愉快开黑 · 评价返图</p></div>
        </div>
    </div>
</div>

<div class="footer">
    陪玩订单系统 GamePlayOrder · 让每一局游戏都有人陪你赢<br>
    <a href="${pageContext.request.contextPath}/playmate/list">陪玩大厅</a> ·
    <a href="${pageContext.request.contextPath}/register">注册账号</a> ·
    <a href="${pageContext.request.contextPath}/playmate/apply">申请成为陪玩师</a>
</div>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
