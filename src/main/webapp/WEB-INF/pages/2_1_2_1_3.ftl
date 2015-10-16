<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>补充注册信息</title>
	<#include "./inc/head.ftl">
</head>
<body>
<div class="layer">
    <#assign border=true>
    <#include "./inc/header.ftl">
    <div class="layerC">
        <div class="layerBody">
            <div class="ttFont">请补充笔名和密码</div>
		<@zanshang.bind "personalForm" />
            <form action="${requestContext.contextPath}/register/connect/${platform}/email" method="post">
                <div class="reg">
                    <ul>
                        <li>
						<@zanshang.formInput "personalForm.name" 'placeholder="笔名(出版时用做署名)" class="txt"'/>
	                            <@zanshang.showErrors "error" />
                        </li>
                        <li>
						<@zanshang.formPasswordInput "personalForm.password" 'placeholder="密码" class="txt"'/>
                        <@zanshang.showErrors "error" />
                        </li>
                        <li>
                            <input type="password" id="confirmPassword" value="" placeholder="确认密码" class="txt">
                        </li>
                    </ul>
                </div>
				<@zanshang.formHiddenInput "personalForm.email"/>
                <label class="lbFont" style="margin:12px 25px">
                    <input type="checkbox">阅读并同意<a target="_blank" href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                <div class="btnList">
                    <input type="submit" class="btn" id="add_info1" value="确定"/>
                </div>
            </form>
        </div>
    </div>
</div>
</div>

<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>