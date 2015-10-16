<#escape x as x?html>
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>浏览</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<script>var server = "${requestContext.contextPath}/";</script>
<body>
    <#assign list=projects.getContent()>
	<#list list as project>
        <div class="author">
            <div class="picBorder">
                <a href="${requestContext.contextPath}/projects/${project.getProjectId()}">
                    <img src="${project.getCover()}?imageView2/2/w/540" />
                </a>
            </div>
            <h3 class="bookName">
                <a href="${requestContext.contextPath}/projects/${project.getProjectId()}">
						${project.getBookName()}</a></h3>
            <ul class="bookState">
                <li class="<#if project.getCurrentMoney() gte project.getGoal()>suc</#if> progress">
						${(project.getCurrentMoney()/project.getGoal() * 100)?floor}%
                    <label>
                        已筹</label>
                </li>
				<#assign aDateTime = .now>
                <li class="<#if project.isDeadline()>suc</#if> time">
						${project.getPassedDays()} / ${project.getTotalDays()}
                    <label>
                        进行天数</label>
                </li>
                <li <#if project.isPublisherLocked()>class="suc"</#if>>/
                    <label>
                        出版支持</label>
                </li>
            </ul>
        </div>
    </#list>
    <div id="loadmore" size="10" page="0">
        加载更多
    </div>
    <#assign menu_projects=true>
    <#include "../inc/menu.ftl">
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</html>
</#escape>