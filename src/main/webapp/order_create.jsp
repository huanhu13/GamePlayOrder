<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>创建订单 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>
<div class="container" style="max-width:760px;">
    <div class="panel">
        <div class="section-head"><h2>预约陪玩 <small>填写预约信息，陪玩师接单后即可开黑</small></h2></div>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <c:choose>
            <c:when test="${empty playmate}">
                <p style="padding:20px 0; color:#aeb9d4;">陪玩师信息不存在，请返回 <a href="${pageContext.request.contextPath}/playmate/list">陪玩大厅</a> 重新选择。</p>
            </c:when>
            <c:otherwise>
                <%-- 陪玩师信息卡 --%>
                <div class="mate-card" style="margin-bottom:22px;">
                    <div class="cover" style="height:130px;">
                        <img src="${pageContext.request.contextPath}/images/game-moba.png" alt="">
                        <span class="online"><i></i>在线接单</span>
                        <span class="cert">✓ 认证</span>
                        <span class="game-tag">${playmate.gameName}</span>
                    </div>
                    <div class="mate-body">
                        <div class="mate-head">
                            <div class="avatar">${fn:substring(playmate.nickname, 0, 1)}</div>
                            <div>
                                <div class="name-row">
                                    <span class="name">${playmate.nickname}</span>
                                    <span class="vip">V${playmate.id % 3 + 3}</span>
                                </div>
                                <div class="area">${playmate.area} · ⭐ ${playmate.avgScore}</div>
                            </div>
                            <div class="price" style="margin-left:auto;">¥${playmate.price}<small> /小时</small></div>
                        </div>
                        <div class="mate-intro">${empty playmate.intro ? '服务态度好，技术过硬，带你轻松赢下每一局！' : playmate.intro}</div>
                    </div>
                </div>

                <%-- 回显默认值：优先取 Servlet 回传的值 --%>
                <form id="orderForm" action="${pageContext.request.contextPath}/order/create" method="post">
                    <input type="hidden" name="playmateId" value="${playmate.id}">
                    <div class="form-item">
                        <label>预约时间</label>
                        <input type="datetime-local" name="appointTime" value="${appointTime}" required>
                    </div>
                    <div class="form-item">
                        <label>时长(小时)</label>
                        <input id="hours" type="number" name="hours" min="1" max="12"
                               value="${not empty hours ? hours : 1}" required>
                    </div>
                    <div class="form-item">
                        <label>备注</label>
                        <textarea name="remark" rows="3" cols="40" placeholder="选填，如：想练打野、希望晚上8点开始">${remark}</textarea>
                    </div>

                    <div class="action-bar">
                        <span class="sum">预计金额<b id="totalSum">¥${playmate.price}</b></span>
                        <a href="${pageContext.request.contextPath}/playmate/list" style="color:#8f9ab5; font-size:13px;">返回大厅</a>
                        <button type="submit" class="btn btn-lg">提交订单 🎮</button>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div class="footer">陪玩订单系统 · 实力陪玩，快乐开黑</div>
<script>
    (function () {
        var price = parseFloat('${playmate.price}') || 0;
        var hours = document.getElementById('hours');
        var sum = document.getElementById('totalSum');
        if (!hours || !sum) { return; }
        var refresh = function () {
            var h = parseFloat(hours.value) || 0;
            sum.textContent = '¥' + (price * h).toFixed(2);
        };
        hours.addEventListener('input', refresh);
        refresh();
    })();
</script>
</body>
</html>
