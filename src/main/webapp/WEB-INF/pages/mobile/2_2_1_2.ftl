<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>邮箱重置密码</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="layer">
        <@zanshang.bind "passwordEmailForm" />
        <form action="${requestContext.contextPath}/account/password/reset" method="post">
        <div class="layerC">
            <div class="layerBody">
                <ul class="tab">
                <li >手机重置</li>
                <li class="cur">邮箱重置</li>
                </ul>
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
            </div>
        </div>
    	</form>
    </div>
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<@zanshang.alertErrors/>
</html>
</#escape>