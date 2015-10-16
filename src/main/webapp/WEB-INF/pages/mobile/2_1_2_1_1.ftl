<#escape x as x?html>﻿
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>补充信息</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <form action="${requestContext.contextPath}/register/connect/${platform}" method="post">
<div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                请补充手机或邮箱
                </div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" name="username"  placeholder="手机/邮箱" class="txt test required" />
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                    <input type="submit" class="btn btntest" value="确定" />
                </div>
            </div>
        </div>
    </div>
    </form>
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
    <#if message??>
    <script>alert("${message}");</script>
    </#if>
</html>
</#escape>