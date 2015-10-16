<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>查看他人消息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <script>var server= "${requestContext.contextPath}/";</script>
</head>
<body>
    <div id="pLetter">
        <div class="pc">
            <div class="zsAuthor">
                <img src="${imageRoot}${setting.getAvatar()}" />
                <label>
                    ${setting.getDisplayName()}</label>
            </div>
            <div class="btnList">
				<#if visitor?? && visitor.getUid() != setting.getUid()>
                	<a class="btn" href="#pLMsg">私信Ta</a>
                </#if>
            </div>
            <ul class="zsData">
                <li>发布项目 <a><#if ownSize??>${ownSize}<#else>0</#if></a></li>
                <li>赞赏项目 <a><#if paidSize??>${paidSize}<#else>0</#if></a></li>
                <li>出版项目 <a><#if publishSize??>${publishSize}<#else>0</#if></a></li>
            </ul>
        </div>
	<#if ownProjects?exists && ownProjects?size gt 0>
        <div class="zsItem">
            <h3>
                发布项目</h3>
            <ul>
				<#list ownProjects as project>
					<#if project.getState() == "REVIEWING" || project.getState() == "PRICING">
                    <#else>
                    <li>
                        <a href="${requestContext.contextPath}/projects/${project.getId()}">
                            <img src="${imageRoot}${project.getCover()}" class="shadow"/>
                            <label title="${project.getBookName()}">
							${project.getBookName()}</label>
                        </a>
                    </li>
                    </#if>
				</#list>
            </ul>
        </div>
	</#if>
	<#if  paidProjects?exists && paidProjects?size gt 0>
        <div class="zsItem">
            <h3>
                赞赏项目</h3>
            <ul>
				<#list paidProjects as project>
                    <li>
                        <a href="${requestContext.contextPath}/projects/${project.getId()}">
                            <img src="${imageRoot}${project.getCover()}" class="shadow"/>
                            <label title="${project.getBookName()}">
							${project.getBookName()}</label>
                        </a>
                    </li>
				</#list>
            </ul>
        </div>
	</#if>
	<#if publishProjects?exists && publishProjects?size gt 0>
        <div class="zsItem">
            <h3>
                出版项目</h3>
            <ul>
				<#list publishProjects as project>
                    <li>
                        <a href="${requestContext.contextPath}/projects/${project.getId()}">
                            <img src="${imageRoot}${project.getCover()}" class="shadow"/>
                            <label title="${project.getBookName()}">
							${project.getBookName()}</label>
                        </a>
                    </li>
				</#list>
            </ul>
        </div>
	</#if>
    </div>
<#if visitor?exists>
	<#if visitor?? && visitor.getUid() != setting.getUid()>
    <div id="pLMsg">
        <ul class="msgList">
			<#list messages.getContent() as message>
                <li <#if message.isMe()>class="my"</#if>>
                    <img src="${imageRoot}${message.getAvatar()}"/>

                    <div class="msgC">
                        <p>
						${message.getMessage()}
                        </p>

                        <div class="msgName">
						${message.getDisplayName()}，<label><@zanshang.until message.getTime() /></label></div>
                    </div>
                </li>
			</#list>
        </ul>
        <div class="inputMsg">
            <div>
                <img src="${imageRoot}${visitor.getAvatar()}"/>
                <!--这里帮忙指定一个profileid-->
                <textarea placeholder="输入聊天内容" profileid="${setting.getUid()}" id="txtMessage"></textarea>
            </div>
            <a class="btn" id="btnMessage">发送</a>
        </div>
    </div>
    </#if>
</#if>
    <#assign menu_profile=true>
    <#include "../inc/menu.ftl">
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</html>
</#escape>