<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>提示信息</title>
	<#include "./inc/head.ftl">
</head>
<body>
<div class="layer">
    <div class="layerC">
        <div class="itemSuc">
            <div class="ttFont">
			${title}</div>
            <div class="sucTip" style="text-align: center;">
			${content}
            </div>
            <div class="btnList">
                <a class="btn" href="<#if goto??>${goto}<#else >${requestContext.contextPath}/</#if>">确定</a>
            </div>
        </div>
    </div>
</div>
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>