<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>登录</title>
	<#include "./inc/head.ftl">
</head>
<body>
    <form action="${requestContext.contextPath}/authentication" method="post">
    <div class="layer">
        <div id="ph">
            <img src="${requestContext.contextPath}/static/img/zanshang.png" id="logo" />
            <ul id="topMenu">
                <li><a href="${requestContext.contextPath}/">首页</a></li>
                <li><a href="${requestContext.contextPath}/projects">浏览</a></li>
                <li><a href="${requestContext.contextPath}/salons">沙龙</a></li>
            </ul>
            <div id="msgRight">
                <#if RequestParameters['return']??>
                    <a class="btn btnReg" href="${requestContext.contextPath}/register?return=${RequestParameters['return']}">注册</a>
                <#elseif return??>
                    <a class="btn btnReg" href="${requestContext.contextPath}/register?return=${return}">注册</a>
                <#else>
                    <a class="btn btnReg" href="${requestContext.contextPath}/register">注册</a>
                </#if>
            </div>
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" id="txt_username" name="username" placeholder="邮箱/手机号" class="txt" autocomplete="off">
                            <#if bad_credentials?exists>
                                <div class="error">错误的用户名或密码</div>
                            </#if>
                        </li>
                        <li>
                            <input type="password" id="txt_password" name="password" placeholder="密码" class="txt" autocomplete="off"><a href="${requestContext.contextPath}/account/password/reset" class="pswdLink">忘记密码？</a>
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                <#if RequestParameters['return']??>
                    <input type="hidden" name="return" id="return" value="${RequestParameters['return']}">
                </#if>
                <#if return??>
                    <input type="hidden" name="return" id="return" value="${return}">
                </#if>
                    <input type="submit" value="登录" class="btn" id="btn_login">
                </div>
                <div class="btnList btnline">
                    <a class="btn btnwb" href="${weibo_redirect_uri}">
                        <img src="${requestContext.contextPath}/static/img/weibo.png" />
                        授权微博登录
                    </a>
                    <a class="btn btnwx" href="${wechat_redirect_uri}">
                        <img src="${requestContext.contextPath}/static/img/weixin.png" />
                        授权微信登录
                    </a>
                </div>
            </div>
        </div>
    </div>
        <#include "./inc/help.ftl">
    </form>
        <script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
    <script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>