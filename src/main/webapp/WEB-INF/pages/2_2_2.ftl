<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>重置密码</title>
	<#include "./inc/head.ftl">
</head>
<body>
    <div class="layer">
        <#assign border=true>
        <#include "./inc/header.ftl">
        <div class="layerC">
            <form action="${requestContext.contextPath}/account/password" method="post">
            <div class="layerBody">
            <div class="ttFont">重置密码</div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="password" name="password" id="password" placeholder="新密码" class="txt" />
                        </li>
                        <li>
                            <input type="password" placeholder="确认密码" class="txt" />
                        </li>
                    </ul>
                </div>
	            <input type="hidden" name="email" value="${email}"/>
	            <input type="hidden" name="code" value="${code}"/>
                <input type="hidden"
                       name="_method_"
                       value="PUT"/>
                <div class="btnList">
	                <input type="submit" class="btn" value="确定"/>
                </div>
            </div>
            </form>
        </div>
    </div>
</body>
</html>
</#escape>