<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>意见反馈</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <script>var server= "${requestContext.contextPath}/";</script>
</head>
<body>

<div class="layer">
    <form action="${requestContext.contextPath}/suggestionfeedback/mobile" method="post">
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont" style="margin-bottom:20px;">
                    意见反馈
                </div>
                <div class="suggestion">
                    您好！为了给您提供更好的服务，我们希望收集您使用赞赏社交出版平台时的看法或建议。对您的配合和支持表示衷心感谢
                </div>
                <div class="suggestion-textarea">
                    <textarea  maxlength="200" name="content"  placeholder="请留下您的问题或意见"></textarea>
                </div>
                <div class="suggestion-input">
                    <input maxlength="50" type="text" name="contact" maxlength="100" placeholder="您的联系方式，如邮箱、手机" />
                </div>
                <div id="submit-btn" class="btnList" style="margin-top:15px;">
                    <input type="submit" class="btn btntest" value="提交">
                </div>
            </div>
        </div>
    </form>
</div>

<#include "../inc/menu.ftl">
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</body>
</html>
