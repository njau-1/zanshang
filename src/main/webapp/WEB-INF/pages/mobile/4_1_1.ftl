<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>沙龙</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <ul class="tab">
        <#if title == "my">
            <li class="cur">我参与的</li>
            <li><a href="${requestContext.contextPath}/salons/all">所有沙龙</a></li>
        </#if>
        <#if title == "all">
            <li><a href="${requestContext.contextPath}/salons/my">我参与的</a></li>
            <li class="cur">所有沙龙</li>
        </#if>
    </ul>

	<#list salons.getContent() as salon>
        <div class="author">
            <div class="picBorder">
                <a href="${requestContext.contextPath}/salons/${salon.getSalonId()}">
                    <img src="${imageRoot}${salon.getImage()}" />
                </a>
            </div>
            <h3 class="bookName">
                <a href="${requestContext.contextPath}/salons/${salon.getSalonId()}">${salon.getName()}</a></h3>
            <ul class="salong">
                <li>${salon.getMembers()}人参与</li>
                <li class="line">|</li>
                <li>${salon.getHistory()}个样稿</li>
                <li class="line">|</li>
                <li>${salon.getChat()}条留言</li>
            </ul>
        </div>
	</#list>

    <div class="pager">
        <a
            <#if salons.hasPrevious()>
                    class="prev"
                    href="${requestContext.contextPath}/salons/${tab}?page=${salons.getNumber()-1}&size=${salons.getSize()}"
            <#else >
                    class="prev noData"
            </#if>
                >上一页</a><a
        <#if salons.hasNext()>
                class="next"
                href="${requestContext.contextPath}/salons/${tab}?page=${salons.getNumber()+1}&size=${salons.getSize()}"
        <#else >
                class="next noData"
        </#if>
            >下一页</a>
    </div>
    <#assign menu_salon=true>
    <#include "../inc/menu.ftl">
</body>
</html>
</#escape>