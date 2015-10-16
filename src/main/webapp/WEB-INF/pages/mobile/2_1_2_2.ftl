<#escape x as x?html>﻿
﻿﻿<#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>注册</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <script>var server = "${requestContext.contextPath}/";</script>
</head>
<body>
<div class="layer">
    <div class="layerC">
        <div class="layerBody">
            <ul class="tab">
                <li class="cur">手机</li>
                <li>邮箱</li>
            </ul>
            <div class="tabHide defaultShow">
                <@zanshang.bind "personalForm" />
                <form action="${requestContext.contextPath}/register/personal/phone" method="post">
                    <div class="reg">
                        <ul>
                            <li>
                                <@zanshang.formInput "personalForm.phone" 'placeholder="手机号" class="txt test required mobile"' />
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "personalForm.name" 'placeholder="笔名(出版时用做署名)" class="txt test required" autocomplete="off"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formPasswordInput "personalForm.password" 'placeholder="密码" class="txt test required" autocomplete="off"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <input type="password"  placeholder="确认密码" class="txt test required confirmPwd"/>
                            </li>
                            <li>
                                <input type="text" placeholder="图片验证码" id="txtCaptcha" class="txt test required" maxlength="10" autocomplete="off"><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image"></a> </li>
                            <li>
                                <@zanshang.formInput "personalForm.code" 'placeholder="验证码" name="code" class="txt test required"'/>
                                <@zanshang.showErrors "error" />
                                <input class="btnSmall btnVCode" type="button" value="获取" />
                            </li>
                        </ul>
                    </div>
                    <label class="lbFont"><input class="test checked" checked="" type="checkbox" />阅读并同意<a href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                    <div class="btnList">
                        <#if RequestParameters['return']??>
                            <input type="hidden" name="return" id="return" value="${RequestParameters['return']}">
                        </#if>
                        <#if return??>
                            <input type="hidden" name="return" id="return" value="${return}">
                        </#if>
                        <input type="submit" class="btn btntest" value="注册" />
                    </div>
                </form>
            </div>
            <div class="tabHide">
                <@zanshang.bind "personalForm" />
                <form action="${requestContext.contextPath}/register/personal/email" method="post">
                    <div class="reg">
                        <ul>
                            <li>
                                <@zanshang.formInput "personalForm.name" 'placeholder="笔名(出版时用做署名)" class="txt test required"'/>
                            <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "personalForm.email" 'placeholder="邮箱" class="txt test required email"'/>
                            <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formPasswordInput "personalForm.password" 'placeholder="密码" class="txt test required"' />
                            <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <input type="password"  placeholder="确认密码" class="txt test required confirmPwd"/>
                            </li>
                        </ul>
                    </div>
                    <label class="lbFont"><input class="test checked" checked="" type="checkbox"/>阅读并同意<a href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                    <div class="btnList">
                        <#if RequestParameters['return']??>
                            <input type="hidden" name="return" id="return" value="${RequestParameters['return']}">
                        </#if>
                        <#if return??>
                            <input type="hidden" name="return" id="return" value="${return}">
                        </#if>
                        <input type="submit" class="btn btntest" value="注册" />
                    </div>
                </form>
            </div>
            <#if RequestParameters['return']??>
                <div class="otherLink"><br /><a href="${requestContext.contextPath}/authentication?return=${RequestParameters['return']}">已有账号？请登录</a></div>
            <#elseif return??>
                <div class="otherLink"><br /><a href="${requestContext.contextPath}/authentication?return=${return}">已有账号？请登录</a></div>
            <#else>
                <div class="otherLink"><br /><a href="${requestContext.contextPath}/authentication">已有账号？请登录</a></div>
            </#if>
        </div>
    </div>
</div>

</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
<@zanshang.alertErrors/>
</html>
</#escape>