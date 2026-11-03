<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>出错了 - 陪玩订单系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container" style="width:520px; max-width:92%; margin-top:80px;">
    <div class="panel" style="text-align:center; padding:46px 30px;">
        <div style="font-size:52px; margin-bottom:10px;">🎮💥</div>
        <h2 style="border:none; padding:0; text-align:center;">这局掉线了</h2>
        <p style="margin:16px 0 26px; color:#aeb9d4; line-height:1.9;">
            抱歉，页面出错了（错误码：${pageContext.errorData.statusCode}）<br>
            可能是链接失效或服务暂时不可用，回城补个状态再战吧。
        </p>
        <a class="btn btn-lg" href="${pageContext.request.contextPath}/index.jsp">返回首页</a>
        <a class="btn btn-lg btn-ghost" href="${pageContext.request.contextPath}/playmate/list">去陪玩大厅</a>
    </div>
</div>
</body>
</html>
