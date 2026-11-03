<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>陪玩师审核 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>
<div class="container">
    <div class="panel">
        <h2>陪玩师申请审核</h2>
        <p style="color:#aeb9d4; font-size:13px; margin-bottom:16px;">
            审核通过后，该陪玩师将立即出现在陪玩大厅并开放下单；驳回后申请人可重新提交资料。</p>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <table>
            <tr>
                <th>ID</th><th>申请人</th><th>擅长游戏</th><th>大区/段位</th>
                <th>价格(元/小时)</th><th>简介</th><th>审核状态</th><th>操作</th>
            </tr>
            <c:forEach var="p" items="${playmateList}">
                <tr>
                    <td>${p.id}</td>
                    <td>${p.nickname}</td>
                    <td>${p.gameName}</td>
                    <td>${p.area}</td>
                    <td>${p.price}</td>
                    <td>${p.intro}</td>
                    <td>
                        <%-- 状态：0待审核 1通过 2驳回 --%>
                        <c:choose>
                            <c:when test="${p.status == 0}"><span class="badge badge-warn">待审核</span></c:when>
                            <c:when test="${p.status == 1}"><span class="badge badge-ok">已通过</span></c:when>
                            <c:otherwise><span class="badge badge-gray">已驳回</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <%-- 仅待审核状态可操作 --%>
                        <c:if test="${p.status == 0}">
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/admin/playmate">
                                <input type="hidden" name="op" value="pass">
                                <input type="hidden" name="id" value="${p.id}">
                                <button class="btn btn-sm btn-success" type="submit">通过</button>
                            </form>
                            <form style="display:inline" method="post"
                                  action="${pageContext.request.contextPath}/admin/playmate">
                                <input type="hidden" name="op" value="reject">
                                <input type="hidden" name="id" value="${p.id}">
                                <button class="btn btn-sm btn-warning" type="submit">驳回</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty playmateList}">
                <tr><td colspan="8">暂无申请记录</td></tr>
            </c:if>
        </table>
    </div>
</div>
</body>
</html>
