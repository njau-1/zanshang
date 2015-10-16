<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>补充注册信息</title>
    <meta name="description"
          content="通过补充手机或邮箱以确定用户是否已经注册"/>
	<#include "./inc/head.ftl">
</head>
<body>
<div class="layer">
    <#assign border=true>
    <#include "./inc/header.ftl">
    <div class="layerC">
        <div class="layerBody">
            <div class="ttFont">请补充手机或邮箱</div>
            <form action="${requestContext.contextPath}/register/connect/${platform}" method="post">
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" name="username" id="username" placeholder="手机/邮箱" class="txt"/>
						<#if message??>
                            <div class="error">${message}</div></#if>
                        </li>
                    </ul>
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