<#escape x as x?html>﻿
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>登录</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <form action="${requestContext.contextPath}/authentication" method="post">
    <div class="layer">
        <div class="btnClose">
        	&nbsp;</div>
        <div class="layerC">
            <div class="layerBody">
                <div class="tt">
                    <img src="${requestContext.contextPath}/static/img/zanshang.png" />
                </div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" placeholder="邮箱/手机号" class="txt test required mobileOrEmail" name="username"  />
                        </li>
                        <li>
                            <input type="password" placeholder="密码" name="password" class="txt test required" /><a href="${requestContext.contextPath}/account/password/reset" class="pswdLink">忘记密码？</a>
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                <#if RequestParameters['return']??>
                    <input type="hidden" name="return" id="return" value="${RequestParameters['return']}">
                </#if>
                <#if return??>
                    <input type="hidden" name="return" id="return" value="<#if return??>${return}</#if>">
                </#if>
                    <input type="submit" class="btn btntest" value="登录" />
                </div>
                <div class="otherLink">
                    <#if RequestParameters['return']??>
                        <a href="${requestContext.contextPath}/register?return=${RequestParameters['return']}">注册新用户</a>
                    <#elseif return??>
                        <a href="${requestContext.contextPath}/register?return=${return}">注册新用户</a>
                    <#else>
                        <a href="${requestContext.contextPath}/register">注册新用户</a>
                    </#if>
                </div>
                <div class="btnList">
                <a class="btn" href="${weibo_redirect_uri}">
                        <img src="${requestContext.contextPath}/static/img/weibo.png" />
                        授权微博登录
                    </a>
                    <#if isWechat??>
                        <a class="btn btnwx" href="${wechat_redirect_uri}">
                            <img src="${requestContext.contextPath}/static/img/weixin.png" />
                            授权微信登录
                        </a>
                    <#else>
                    </#if>
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