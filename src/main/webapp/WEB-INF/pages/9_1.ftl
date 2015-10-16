﻿<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>消息中心</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="logList">
        <div class="btnRight">
            <label>
                <input type="checkbox" class="ck"/>全选</label>
            <a class="btn">标记为已读</a>
        </div>
        <table cellpadding="0" cellspacing="0" class="table">
		<#list content.getContent() as notification>
            <tr <#if notification.isRead()>class="suc"</#if>>
                <td>
                    <label>
                        <!-- 帮忙绑定orderid在多选框的id属性上-->
                        <#noescape>
                        <#if notification.isRead()>
                            ${notification.getContent()}
                        <#else>
                            <input type="checkbox" id="${notification.getId()}"/>${notification.getContent()}
                        </#if>
                        </#noescape>
                    </label>
                </td>
                <td class="time">
				${notification.getCreateTime()?date}
                </td>
            </tr>
		</#list>
        </table>
        <div class="pager">
            <a
		    <#if content.hasPrevious()>
                    class="prev"
                    href="${requestContext.contextPath}/notifications?page=${content.getNumber()-1}&size=${content.getSize()}"
		    <#else >
                    class="prev noData"
		    </#if>
                    >上一页</a><a
	    <#if content.hasNext()>
                class="next"
                href="${requestContext.contextPath}/notifications?page=${content.getNumber()+1}&size=${content.getSize()}"
	    <#else >
                class="next noData"
	    </#if>
                >下一页</a>
        </div>
    </div>
</div>
<#include "./inc/footer.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>