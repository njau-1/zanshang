<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>修改手机</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <form action="${requestContext.contextPath}/mobile/settings/phone" method="post">
<div class="layer">
        <div class="layerC">
            <div class="layerBody">
                 <div class="ttFont">
                修改手机
                </div>
                <div class="reg">
                     <ul>
                        <li>
                            <input type="text" placeholder="手机号" name="phone" class="txt test required mobile" id="txtMob" />
                        </li>
                         <li>
                             <input type="text" placeholder="图片验证码" id="txtCaptcha" class="txt test required" maxlength="10" /><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image"/></a>
                         </li>
                         <li>
                            <input type="text" placeholder="验证码" name="code" class="txt test required"" /><input class="btnSmall btnVCode" type="button" value="获取" />
                        </li>
                    </ul>
                   
                </div>
               
                <div class="btnList">
                    <input type="submit" class="btn btntest" value="确定">
                </div>
            </div>
        </div>
    </div>
    </form>
  <script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
  <script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
  <script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
  <#if error?? && error?has_content>
    <script>alert("${error}");</script>
  </#if>
</body>
</html>
</#escape>