<#escape x as x?html>
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>设置新密码</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <form action="${requestContext.contextPath}/account/password" method="post">
<div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
             <div class="ttFont">重置密码</div>
           <div class="bz">邮箱：${email}</div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="password" name="password" placeholder="新密码" class="txt test required"/>
                        </li>
                         <li>
                            <input type="password" placeholder="确认密码" class="txt test required confirmPwd"/>
                        </li>
                    </ul>
                </div>
	            <input type="hidden" name="email" value="${email}"/>
	            <input type="hidden" name="code" value="${code}"/>
                <input type="hidden"
                       name="_method_"
                       value="PUT"/>
                <div class="btnList">
	                <input type="submit" class="btn btntest" value="确定"/>
                </div>
            </div>
        </div>
    </div>
    </form>
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
</html>
</#escape>