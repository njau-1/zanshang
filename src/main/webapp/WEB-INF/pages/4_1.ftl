<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>沙龙浏览</title>
    <meta name="description"
          content="发起项目的作者沙龙"/>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#assign header_salon=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="salonList">
	<#if joinedSalons?exists && joinedSalons.getContent()?size gt 0>
        <div class="item">
            <div class="type">
                <div class="tt">
                    我参与的沙龙
                </div>
                <a class="btn btnSlide" href="${requestContext.contextPath}/salons/my">展开</a>
            </div>
			<#list joinedSalons.getContent() as salon>
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
	</#if>
	<#if allSalons?exists>
        <div class="item">
            <div class="type">
                <div class="tt">
                    全部沙龙
                </div>
                <a class="btn btnSlide" href="${requestContext.contextPath}/salons/all">展开</a>
            </div>
			<#list allSalons.getContent() as salon>
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
	</#if>
    </div>
</div>
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>