<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>申请陪玩师 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="/common/nav.jsp" %>
<div class="container" style="max-width:760px;">
    <div class="panel">
        <h2>申请成为陪玩师 💰</h2>
        <p style="color:#aeb9d4; font-size:13px; margin-bottom:18px; line-height:1.8;">
            零门槛入驻，边玩游戏边赚钱！提交申请后由管理员审核，通过即上架陪玩大厅，订单收入平台仅抽成 20%。</p>
        <c:if test="${not empty msg}">
            <div class="msg">${msg}</div>
        </c:if>
        <c:choose>
            <c:when test="${not empty myPlaymate}">
                <%-- 已提交过申请：展示审核状态 --%>
                <p>您已提交过申请：</p>
                <p style="margin-top:10px;">
                    擅长游戏：<b>${myPlaymate.gameName}</b>（${myPlaymate.area}） |
                    价格：<b>${myPlaymate.price} 元/小时</b> |
                    审核状态：
                    <c:choose>
                        <c:when test="${myPlaymate.status == 0}"><span style="color:#e17055;">待审核</span></c:when>
                        <c:when test="${myPlaymate.status == 1}"><span style="color:#00b894;">已通过</span></c:when>
                        <c:otherwise><span style="color:#b2bec3;">已驳回</span></c:otherwise>
                    </c:choose>
                </p>
            </c:when>
            <c:when test="${loginUser.role == 2}">
                <p>管理员账号无需申请陪玩师。</p>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/playmate/apply" method="post">
                    <div class="form-item">
                        <label>擅长游戏</label>
                        <input type="text" name="gameName" value="${gameName}" required placeholder="如：王者荣耀">
                    </div>
                    <div class="form-item">
                        <label>大区/段位</label>
                        <input type="text" name="area" value="${area}" required placeholder="如：微信区·王者50星">
                    </div>
                    <div class="form-item">
                        <label>价格(元/小时)</label>
                        <input type="number" name="price" step="0.01" min="0.01" value="${price}" required>
                    </div>
                    <div class="form-item">
                        <label>个人简介</label>
                        <textarea name="intro" rows="4" cols="40" placeholder="选填">${intro}</textarea>
                    </div>
                    <div class="form-item">
                        <label></label>
                        <button type="submit" class="btn">提交申请</button>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
