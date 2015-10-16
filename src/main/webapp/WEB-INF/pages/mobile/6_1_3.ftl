<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>赞赏人列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        
    </style>
</head>
<body>
    <form action="" method="post">
    <div class="zsUserList" id="zsUserList">
        <div class="pc">
            <h3>
                <a href="${requestContext.contextPath}/projects/${projectId}">${bookName}</a></h3>
            <h4>
                <a href="">赞赏人</a></h4>
            <div class="pagers">
            <#assign pager=["壹","贰","叁","肆","伍","陆","柒","捌","玖","拾"]>
                <a >全部</a>
            <#list buyerMap?keys as key>
                <a <#if index?? && index == key_index>class="btn"</#if>>${pager[key_index]}</a>
            </#list>
            </div>
            <div class="slnBorder">
            <#list buyerMap?keys as key>
                <ul class="slnUser" <#if index?? && index == key_index>style="display: block;"</#if>>
                    <#list buyerMap[key] as buyer>
                        <li>
                            <a href="${requestContext.contextPath}/profile/${buyer.getUid()}"><img
                                    src="${imageRoot}${buyer.getAvatar()}?imageView2/2/w/60/h/60"/></a>
                            <label>
                            ${buyer.getDisplayName()}</label>
                        </li>
                    </#list>
                </ul>
            </#list>
            </div>
        </div>
    </div>
    </form>
    <script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
    <script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</body>
</html>
