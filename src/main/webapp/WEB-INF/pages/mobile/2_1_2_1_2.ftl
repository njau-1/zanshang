<#escape x as x?html>﻿
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>老用户登录</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <form action="${requestContext.contextPath}/register/connect/${platform}/account" method="post">
<div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                老用户登录
                </div>
                <div class="bz">账户:<span>${username}</span></div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="password" name="password"  placeholder="密码" class="txt test required" />
                        </li>
                    </ul>
	                <input type="hidden" name="username" id="username" value="${username}"/>
                </div>
                <div class="btnList">
                    <input type="submit" value="确定" class="btn btntest" />
                </div>
            </div>
        </div>
    </div>
    </form>
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<#if bad_credentials?exists>
<script>alert("错误的用户名或密码");</script>
</#if>
</html>
</#escape>