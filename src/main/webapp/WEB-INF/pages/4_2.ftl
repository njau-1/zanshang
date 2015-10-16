<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>沙龙详情</title>
	<#list projects as project>
        <meta name="description"
              content="${project.getDescription()}"/>
        </p>
		<#if project_has_next>
			<#break >
		</#if>
	</#list>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#assign header_salon=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="slDetails">
        <div class="left">
            <div class="flx">
                <div class="dc">
				<#if currentTopic?exists>
                    <h4 class="tt">
					${currentTopic.getTitle()}</h4>

                    <div id="slTime">
                        <!-- 距离样稿消失时间：<label> {aliveTime}</label> --></div>
					<#if currentTopic.getImages()?size gt 0>
	                    <div class="pic imgView" style="display: block;">
                            <#list currentTopic.getImages() as image>
                                <img src="${imageRoot}${image}?imageView2/2/w/580/h/290"/>
                            </#list>
	                    </div>
					</#if>
                    <p class="pre">${currentTopic.getContent()}</p>
				<#else >
                    <div class="empty">
                        <h3>作者还没发出新样稿，每个样稿仅能存活24小时。</h3>

                        <div class="btnList">
                            <a class="btn" href="${requestContext.contextPath}/profile/${salon.getUid()}#pLMsg">私信作者</a>
                        </div>
                    </div>
				</#if>
                </div>
            </div>
        </div>
        <div class="right">
            <div class="dc">
                <div class="bookTopSm">
                    <#if projects[0].getState() == "PUBLISHING" ||  projects[0].getState() == "SUCCESS">
                    <#assign progressStrs=["选题申报","一审","二审","排版","一校","二校","三校","终审","申请CIP","质检","下厂印刷"]>
                    <a id="btnprogress" class="btn">出版进度
                        <div id="btnMenu">
                            <div>
                                <ul>
                                    <#list progressStrs as progressStr>

                                        <li <#if progressCode gte progressStr_index>class="suc"</#if>>${progressStr}</li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </a>
                    </#if>
                    <#list projects as project>
                    <a href="${requestContext.contextPath}/projects/${project.getId()}">
                    	<img src="${imageRoot}${project.getCover()}?imageView2/2/w/84/h/120" class="bookCover shadow"/>
                    </a>
                <div class="bookMsg">
                    <h3>
						<a class="link_bookname" href="${requestContext.contextPath}/projects/${project.getId()}">
							《${project.getBookName()}》,${authorname}
						</a>
                    </h3>
                    <p class="pre">${project.getDescription()?html}</p>
					<#if project_has_next>
						<#break >
					</#if>
				</#list>

                </div>
                </div>
			<#if chats.getContent()?size gt 0>
                <ul class="msgList">
					<#list chats.getContent() as chat>
                        <li <#if visitor?? && visitor.getUid() == chat.getMemberId()>class="my"</#if>>
                            <a href="${requestContext.contextPath}/profile/${chat.getMemberId()}"><img src="${imageRoot}${chat.getAvatar()}"/></a>
                            <div class="msgC">
                                <p>
								${chat.getChat()}
                                </p>

                                <div class="msgName">
								${chat.getName()}，<label><@zanshang.until chat.getCreateTime()/></label></div>
                            </div>
                        </li>
					</#list>
                </ul>
			<#else >
                <div class="empty">发起沙龙里的第一句话吧…</div>
			</#if>
            </div>
        </div>
    </div>
</div>
<#if isAuthor || isMember>
<div id="bottomFlx">
    <div class="inputMsg">
        <img src="${imageRoot}${visitor.getAvatar()}"/>

        <div>
            <textarea placeholder="输入聊天内容"></textarea>
        </div>
        <a class="btn" salonid="${salon.getUid()}">发送</a>
    </div>
</div>
</#if>
<div class="bottomTools">
<#if isAuthor>
    <a class="btn" href="${requestContext.contextPath}/salons/${salon.getUid()}/topic">发布样稿</a>
</#if>
    <div class="shareMenu">
        <ul class="shadow">
            <li><a class="sweibo" target="_blank"
                   href="<@zanshang.shareWB shareLink "分享一个沙龙" />">分享到微博</a></li>
            <li><a class="sweixin" openurl="#wechatShare">分享到微信</a></li>
            <li><a class="sdouban" target="_blank"
                   href="<@zanshang.shareDB shareLink "分享一个沙龙" />">分享到豆瓣</a></li>
        </ul>
        <a class="btnShare">&nbsp;</a>
    </div>
</div>
<#if !(isAuthor || isMember)>
	<div id="fixTip">赞赏${authorname}的书就可以与${authorname}和其他读者讨论样稿了！<a class="close">&nbsp;</a></div>
</#if>
<#assign wechatLink=shareQRLink>
<#include "./inc/wechatShare.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/jquery.slides.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>