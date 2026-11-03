<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>结算记录 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>
<div class="container">
    <div class="panel">
        <h2>结算记录</h2>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <table>
            <tr>
                <th>订单号</th><th>陪玩师</th><th>订单总金额(元)</th>
                <th>平台抽成比例</th><th>平台收入(元)</th><th>陪玩师收入(元)</th><th>结算时间</th>
            </tr>
            <c:forEach var="s" items="${settlementList}">
                <tr>
                    <td>${s.orderNo}</td>
                    <td>${s.playmateNickname}</td>
                    <td style="color:#ffd166; font-weight:bold;">¥${s.totalMoney}</td>
                    <td><span class="badge badge-info">${s.platformRate * 100}%</span></td>
                    <td style="color:#22d3ee;">¥${s.totalMoney - s.playmateIncome}</td>
                    <td style="color:#55efc4; font-weight:bold;">¥${s.playmateIncome}</td>
                    <td><fmt:formatDate value="${s.settleTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty settlementList}">
                <tr><td colspan="7">暂无结算记录</td></tr>
            </c:if>
        </table>
    </div>
</div>
</body>
</html>
