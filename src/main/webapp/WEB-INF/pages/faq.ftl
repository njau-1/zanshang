<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>帮助中心</title>
    <meta name="description" content="“赞赏”，中国第一社交出版平台，为“人人都赞赏成书”而创建。不“赞赏”，无思想。"/>
<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#include "./inc/header.ftl" />
<div id="pc">
    <div id="media">
        <ul id="menu_about">
            <li><a href="${requestContext.contextPath}/support/about">关于赞赏</a></li>
            <li><a href="${requestContext.contextPath}/support/media">媒体报道</a></li>
            <li><a  href="${requestContext.contextPath}/support/join">加入我们</a></li>
            <li><a  class="cur">帮助中心</a></li>
            <li><a  href="${requestContext.contextPath}/support/wechat">微信</a></li>
        </ul>
        <div id="new_about">
            <ul class="help">
                <li class="help-list1">注册登录</li>
                <li class="help-list2">赞赏人</li>
                <li class="help-list3">赞赏作者</li>
                <li class="help-list4">出版自由人</li>
                <li class="help-right">
                    <img src="${requestContext.contextPath}/static/img/problem.png" />
                    <sp>意见反馈</sp>
                </li>
            </ul>
            <ul class="help-content1">
            </ul>
        </div>
    </div>
</div>
<div class="Feedback">
    <div class="FeedbackClose">
    </div>
    <div class="comments">
        <div class="comments-message">
            <p>您好！为了给您提供更好的服务，我们希望收集您使用赞赏社交出版平台时的看法或建议。对您的配合和支持表示衷心感谢！</p>
            <textarea class="suggestion-textarea" maxlength="300" style="max-height:200px" placeholder="请写下你想对赞赏说的话"></textarea>
            <input class="suggestion-input" maxlength="50" type="text" placeholder="您的联系方式，如邮箱、手机" />
            <div class="comments-submit"><a class="submit-btn">提交</a></div>
        </div>
    </div>
    <div class="comments-success">
        感谢您的建议！我们会及时给您反馈并不断完善产品。
    </div>

</div>

<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/help.js" type="text/javascript"></script>
</body>
</html>
