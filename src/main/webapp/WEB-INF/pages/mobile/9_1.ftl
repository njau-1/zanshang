<#escape x as x?html>﻿
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>消息中心</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <form action="" method="post">
    <div id="pc">
    <div class="btnRight read">
    <a class="btn">全部标记为已读</a>
    </div>
        <ul class="logList">
			<#list content.getContent() as notification>
	            <li <#if notification.isRead()>class="suc"</#if>>
	                <label>
                        <#noescape>
                            <#if notification.isRead()>
                            ${notification.getContent()}
                            <#else>
                                <input type="checkbox" id="${notification.getId()}"/>${notification.getContent()}
                            </#if>
                        </#noescape>
	                    <span>
							${notification.getCreateTime()?date}
						</span>
	                </label>
	            </li>
			</#list>
        </ul>
        <div class="pager">
            <a class="prev"
		    <#if content.hasPrevious()>
                    href="${requestContext.contextPath}/profile/${tab}?page=${content.getNumber()-1}&size=${content.getSize()}"
		    </#if>>上一页</a> 
		    <a class="next"
		    <#if content.hasNext()>
	                href="${requestContext.contextPath}/profile/${tab}?page=${content.getNumber()+1}&size=${content.getSize()}"
		    </#if>>下一页</a>
        </div>
    </div>
    </form
    <#assign menu_notifications=true>
    <#include "../inc/menu.ftl">
</body>
</html>
</#escape>
