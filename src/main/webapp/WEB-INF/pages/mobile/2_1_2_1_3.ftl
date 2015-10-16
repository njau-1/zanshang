<#escape x as x?html>﻿
﻿<#import "/libs/zanshang.ftl" as zanshang/>
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>新用户邮箱注册</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
<@zanshang.bind "personalForm" />
  <form action="${requestContext.contextPath}/register/connect/${platform}/email" method="post">
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
						<@zanshang.formInput "personalForm.name" 'placeholder="笔名(出版时用做署名)" class="txt test required"'/>
	                            <@zanshang.showErrors "error" />
                        </li>
                        <li>
						<@zanshang.formPasswordInput "personalForm.password" 'placeholder="密码" class="txt test required"'/>
                        <@zanshang.showErrors "error" />
                        </li>
                    </ul>
                   
                </div>
                
                  <label class="lbFont"><input type="checkbox"/>阅读并同意<a href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                  
				<@zanshang.formHiddenInput "personalForm.email"/>
                <div class="btnList">
                    <input type="submit" class="btn btntest" value="确定"/>
                </div>
            </div>
        </div>
    </div>
    </form>
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<@zanshang.alertErrors/>
</html>
</#escape>
