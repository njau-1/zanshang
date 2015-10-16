<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>补充注册信息</title>
	<#include "./inc/head.ftl">
</head>
<body>
<div class="layer">
    <#assign border=true>
    <#include "./inc/header.ftl">
    <div class="layerC">
        <div class="layerBody">
            <div class="ttFont">您已注册，请直接登录</div>
            <form action="${requestContext.contextPath}/register/connect/${platform}/account" method="post">
                <div class="reg">
                    <ul>
                        <li>
                            <input type="password" name="password" id="password" placeholder="密码" class="txt"/>
	                        <#if bad_credentials?exists>
	                            <div class="error">错误的用户名或密码</div>
	                        </#if>
                        </li>
                    </ul>
	                <input type="hidden" name="username" id="username" value="${username}"/>
                </div>
                <div class="btnList">
                    <input type="submit" class="btn" value="确定" />
                </div>
            </form>
        </div>
    </div>
</div>
</div>

<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>