<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${project.getAuthorName()}-${project.getBookName()}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div id="pc">
        <div id="bookDetails">
            <#if project.getImages()?has_content>
                <#if project.getImages()?size gt 0>
                    <div class="imgView">
                        <#list project.getImages() as image>
                            <li>
                                <img src="${imageRoot}${image}?imageView2/2/w/706/h/300"/>
                            </li>
                        </#list>
                    </div>
                </#if>
            </#if>
            <h4 class="tt">
                关于我</h4>
            <#list authors as author>
                <div class="zsAuthor">
                    <a href="${requestContext.contextPath}/profile/${author.getUid()}">
                        <img src="${imageRoot}${author.getAvatar()}"/>
                    </a>
                    <label>
                    ${author.getName()}</label>
                </div>
                <p class="pre">${author.getDescription()}</p>
            </#list>
            <#if project.getOutline()?has_content>
                <h4 class="tt">
                    目录大纲</h4>
                <div class="catalog pre">${project.getOutline()}</div>
            </#if>
        </div>
    </div
    <#assign menu_projects=true>
    <#include "../inc/menu.ftl">
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/jquery.slides.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</html>
</#escape>