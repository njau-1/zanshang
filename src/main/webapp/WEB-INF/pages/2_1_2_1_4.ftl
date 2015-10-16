<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>补充注册信息</title>
	<#include "./inc/head.ftl">
    <script>var server = "${requestContext.contextPath}/";</script>
</head>
<body>
<div class="layer">
    <#assign border=true>
    <#include "./inc/header.ftl">
    <div class="layerC">
        <div class="layerBody">
            <div class="ttFont">请补充昵称和密码</div>
		<@zanshang.bind "personalPhoneForm" />
            <form action="${requestContext.contextPath}/register/connect/${platform}/phone" method="post">
                <div class="reg">
                    <ul>
                        <li>
						<@zanshang.formInput "personalPhoneForm.name" 'placeholder="笔名(出版时用做署名)" class="txt"'/>
	                            <@zanshang.showErrors "error" />
                        </li>
                        <li>
						<@zanshang.formPasswordInput "personalPhoneForm.password" 'placeholder="密码" class="txt"'/>
                        <@zanshang.showErrors "error" />
                        </li>
                        <li>
                            <input type="password" id="confirmPassword" value="" placeholder="确认密码" class="txt">
                        </li>
                        <li>
                            <input type="text" placeholder="图片验证码" id="txtCaptcha" class="txt" maxlength="10" /><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image" /></a>
                        </li>
                        <li>
	                        <input type="text" name="code" id="code" placeholder="短信验证码" class="txt"/>
	                        <@zanshang.bind "personalPhoneForm.code"/>
                            <input type="button" class="btnSmall btnVCode" value="获取"/>
						<@zanshang.showErrors "error" />
                        </li>
                    </ul>
                </div>
                <input id="txtMob" name="phone" type="hidden" placeholder="手机" value="${phone}" class="txt"/>

                <label class="lbFont" style="margin:12px 25px">
                    <input type="checkbox">阅读并同意<a target="_blank" href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                <div class="btnList">
                    <input type="submit" class="btn" id="add_info2" value="确定"/>
                </div>
            </form>
        </div>
    </div>
</div>
</div>

<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
</body>
</html>
</#escape>