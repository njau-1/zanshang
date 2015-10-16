<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>更多沙龙</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#assign header_salon=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="salonList">
        <div class="item">
            <div class="type">
                <div class="tt">
				<#if title == "my">
                    我参与的沙龙
				</#if>
				<#if title == "all">
                    全部沙龙
				</#if>
                </div>
            </div>
            <div id="salongCtr">
	            <#list salons.getContent() as salon>
                    <div class="salong">
                        <a href="${requestContext.contextPath}/salons/${salon.getSalonId()}">
                            <div>
                                <img src="${imageRoot}${salon.getImage()}?imageView2/2/w/285/h/432"/>
                            </div>
                            <h3>
				            ${salon.getName()}</h3>
                            <ul>
                                <li>${salon.getMembers()}人参与</li>
                                <li class="line">|</li>
                                <li>${salon.getHistory()}个样稿</li>
                                <li class="line">|</li>
                                <li>${salon.getChat()}条留言</li>
                            </ul>
                        </a>
                    </div>
	            </#list>
            </div>
        </div>
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
    </div>
</div>
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>