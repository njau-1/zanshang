<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html xmlns:wb="http://open.weibo.com/wb">
<head>
    <title>赞赏成功</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="zsSuc">
        <div class="zsAuthor">
            <img src="${imageRoot}${author.getAvatar()}"/>
            <label>
			${author.getDisplayName()}</label>
        </div>
        <p>
            感谢您，赞赏成功！
            <#if noactive?? && noactive == true>
                <label>您的赞赏账户${email}已建立，系统已向您发出设置密码邮件，请尽快接收邮件设置密码。</label>
            <#else>
                <label>您可以进入沙龙并参与话题讨论了</label>
            </#if>
        </p>

        <div class="btncenter">
            <a class="btn" href="${requestContext.contextPath}/salons/${author.getUid()}">进入沙龙</a>
        </div>
        <div class="shareList">
            <ul>
                <li><a class="sharewx" openurl="#wechatShare"></a>
                    <label>
                        微信</label></li>
                <li><a class="sharewb" target="_blank" href="<@zanshang.shareWB "${shareLink}" "我赞赏了${author.getDisplayName()}的《${project.getBookName()}》"/>"></a>
                    <label>
                        微博</label></li>
                <li><a class="sharedb" target="_blank"
                       href="<@zanshang.shareDB "${shareLink}" "我赞赏了《${project.getBookName()}》，${author.getDisplayName()}就要出书啦！" />"></a>
                    <label>
                        豆瓣</label></li>
            </ul>
            <div class="bookTip">
                *如您有图书质量、物流等问题需咨询，请直接私信作者。
            </div>
        </div>
    </div>
</div>
<#include "./inc/footer.ftl">
<#assign wechatLink=shareQRLink>
<#include "./inc/wechatShare.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>
</#escape>