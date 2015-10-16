<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>手机重置密码</title>
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
                    <li class="cur">手机重置</li>
                    <li>邮箱重置</li>
                </ul>
                <div class="tabHide defaultShow">
                    <form action="${requestContext.contextPath}/account/password" method="post">
                    <div class="reg">
                        <@zanshang.bind "passwordPhoneForm" />
                        <ul>
                            <li>
                                <input type="text" name="phone" placeholder="手机号" class="txt test required mobile" id="phone"/>
                                <@zanshang.bind "passwordPhoneForm.phone"/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <input type="text" placeholder="图片验证码" name="captcha" id="txtCaptcha" class="txt test required" maxlength="10" /><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image" /></a>
                            </li>
                             <li>
                                <@zanshang.formInput "passwordPhoneForm.code" 'placeholder="短信验证码" class="txt test required"'/>
                                 <input class="btnSmall btnVCode" type="button" value="获取" />
                            </li>
                            <li>
                                <@zanshang.formPasswordInput "passwordPhoneForm.rawPassword" 'placeholder="新密码" class="txt test required"' />
                                    <@zanshang.showErrors "error" />
                            </li>
                        </ul>
                    </div>

                    <div class="btnList">
                    <input type="hidden"
                           name="_method_"
                           value="PUT" />
                        <input type="submit" class="btn btntest" value="重置密码" />
                    </div>
                    </form>
                </div>
                <div class="tabHide">
                    <@zanshang.bind "passwordEmailForm" />
                    <form action="${requestContext.contextPath}/account/password/reset" method="post">
                        <div class="reg">
                            <ul>
                                <li>
                                    <@zanshang.formInput "passwordEmailForm.email" 'placeholder="邮箱" class="txt test required"'/>
                                <@zanshang.showErrors "error" />
                                </li>
                            </ul>
                        </div>
                        <div class="btnList">
                            <input type="submit" class="btn btntest" value="发送验证邮件"/>
                        </div>
                    </form>
                </div>
            </div>
    </div>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<@zanshang.alertErrors/>
</body>
</html>
</#escape>
