<#escape x as x?html>﻿
﻿<#import "/libs/zanshang.ftl" as zanshang/>
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>新用户手机注册</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <script>var server = "${requestContext.contextPath}/";</script>
</head>
<body>
<@zanshang.bind "personalPhoneForm" />
  <form action="${requestContext.contextPath}/register/connect/${platform}/phone" method="post">
<div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                新用户注册
                </div>
                <div class="reg">
                    <ul>
                        <li>
                        <@zanshang.formInput "personalPhoneForm.name" 'placeholder="笔名(出版时用做署名)" class="txt test required"'/>
                        <@zanshang.showErrors "error" />
                        </li>
                        <li>
						<@zanshang.formPasswordInput "personalPhoneForm.password" 'placeholder="密码" class="txt test required"'/>
                        <@zanshang.showErrors "error" />
                        </li>
                        <li>
                            <input type="text" placeholder="图片验证码" id="txtCaptcha" class="txt test required" maxlength="10" /><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image"/></a>
                        </li>
                         <li>
						<@zanshang.formInput "personalPhoneForm.code" 'placeholder="短信验证码" class="txt test required"'/>
                             <input class="btnSmall btnVCode" type="button" value="获取" />
						<@zanshang.showErrors "error" />
                        </li>
                    </ul>
                </div>
                <input id="phone" name="phone" type="hidden" placeholder="手机" value="${phone}" class="txt"/>

                  <label class="lbFont"><input class="test checked" type="checkbox" checked/>阅读并同意<a href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                <div class="btnList">
                    <input type="submit" class="btn btntest" value="确定"/>
                </div>
            </div>
        </div>
    </div>
    </form>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<@zanshang.alertErrors/>
</body>
</html>
</#escape>