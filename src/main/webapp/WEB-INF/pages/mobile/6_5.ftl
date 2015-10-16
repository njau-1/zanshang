<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
﻿<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>赞赏</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<#if alipay??>
    <div style="display:none;">
		<#assign keys=alipay?keys>
        <form id="alipay" action="https://mapi.alipay.com/gateway.do?_input_charset=${alipay["_input_charset"]}">
			<#list keys as key>
                <input type="hidden" name="${key}" value="${alipay[key]}"/>
			</#list>
        </form>
    </div>
	</#if>
	<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
	<#if wechat??>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
    <script>
		<#assign keys=wechat?keys>
	    wx.ready(
			    function(){
			    wx.chooseWXPay({
				    <#list keys as key>
				    ${key}:'${wechat[key]}',
				    </#list>
		                complete: function (res) {
		                if (res.errMsg == "chooseWXPay:ok") {
		                    setTimeout(function () {
		                        window.location.href = "${requestContext.contextPath}${callback}";
		                    }, 500);
		                } else {
		                    window.location.href = "${requestContext.contextPath}/payments/${paymentId}/micromessenger/crosspay";
		                }
		            }
            })});

        $(document).ready(function () {
		    <#assign keys=config?keys>
            wx.config({
		    <#list keys as key>
		    ${key}: '${config[key]}',
		    </#list>
                debug:false,
                jsApiList:['chooseWXPay']
            });
        })
        ;
    </script>
	</#if>
	<#if alipay??>
    <script>
        $(document).ready(function () {
            $('#alipay').submit();
        });
    </script>
	</#if>
</body>
</html>
</#escape>