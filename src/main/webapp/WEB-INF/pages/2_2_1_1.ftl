<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>忘记密码</title>
	<#include "./inc/head.ftl">
</head>
<body>
	<script>var server = "${requestContext.contextPath}/";</script>
    <div class="layer">
        <#assign border=true>
        <#include "./inc/header.ftl">
        <div class="layerC">
            <div class="layerBody">
                <ul class="tab">
                    <!-- 当前选中的选项卡请设一个class="cur"-->
                    <li class="cur">手机验证</li>
                    <li>邮箱验证</li>
                </ul>
                <div class="tabHide defaultShow">
                <@zanshang.bind "passwordPhoneForm" />
                    <form action="${requestContext.contextPath}/account/password" method="post">
                        <div class="reg">
                        <ul>
                            <li>
	                            <input id="txtMob" name="phone" placeholder="手机" class="txt"/>
                                <@zanshang.bind "passwordPhoneForm.phone"/>
	                            <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <input type="text" placeholder="图片验证码" id="txtCaptcha" class="txt" maxlength="10" /><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image" /></a>
                            </li>
                            <li>
	                            <input type="text" id="code" name="code" placeholder="短信验证码" class="txt"/>
		                        <input type="button" class="btnSmall btnVCode" value="获取"/>
	                            <@zanshang.bind "passwordPhoneForm.code" />
	                            <@zanshang.showErrors "error" />
                            </li>
	                        <li>
	                            <@zanshang.formPasswordInput "passwordPhoneForm.rawPassword" 'placeholder="新密码" class="txt"' />
                                <@zanshang.showErrors "error" />
	                        </li>
                            <li>
                                <input type="password" id="confirmPassword" value="" placeholder="确认密码" class="txt">
                            </li>
                        </ul>
                    </div>
						<input type="hidden"
								name="_method_"
								value="PUT" />
                    <div class="btnList">
                        <input type="submit" class="btn" id="btn_resetPW" value="重置密码" />
                    </div>
                    </form>
                </div>
                <div class="tabHide">
                    <@zanshang.bind "passwordEmailForm" />
                    <form action="${requestContext.contextPath}/account/password/reset" method="post">
                             <div class="reg">
                        <ul>
                            <li>
                                <@zanshang.formInput "passwordEmailForm.email" 'placeholder="邮箱" class="txt"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                        </ul>
                    </div>
                    <div class="btnList">
                        <input type="submit" class="btn" value="发送验证邮件" id="btn_vemail"/>
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