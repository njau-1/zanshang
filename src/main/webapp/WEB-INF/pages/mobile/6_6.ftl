<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>我赞赏了${author.getDisplayName()}的《${project.getBookName()}》，你呢？</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="sharelayer">
    <div id="shareTip">
        <label> 打开右上角微信分享</label>
    </div>
</div>
<div style="display: none;">
    <img src="${imageRoot}${project.getCover()}?imageView2/1/w/400/h/400"/>
</div>
    <div id="pc">
        <div class="zsuc">
            <div class="zsAuthor">
                <img src="${imageRoot}${author.getAvatar()}">
                <label>${author.getDisplayName()}</label>
            </div>
            <h4>感谢您，赞赏成功！</h4>
            <#if noactive?? && noactive == true>
                <h5>您的赞赏账户${email}已建立，系统已向您发出设置密码邮件，请尽快接收邮件设置密码。</h5>
            <#else>
                <h5>您可以进入沙龙并参与话题讨论了</h5>
            </#if>
            <div class="btnList">
                <a class="btn" href="${requestContext.contextPath}/salons/${author.getUid()}">进入沙龙</a>
            </div>
            <div class="shareList">
	            <a class="weixin" id="btn_wx"></a>
	            <a class="weibo" target="_blank" href="<@zanshang.shareWB "${shareLink}" "我赞赏了${author.getDisplayName()}的《${project.getBookName()}》"/>"></a>
	            <a class="douban" target="_blank" href="<@zanshang.shareDB "${shareLink}" "我赞赏了《${project.getBookName()}》，${author.getDisplayName()}就要出书啦！" />"></a>
            </div>
            <div class="tip">
                *如您有图书质量、物流等问题需咨询，请直接私信 作者</div>
        </div>
    </div>
    <#include "../inc/menu.ftl">
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</html>
</#escape>