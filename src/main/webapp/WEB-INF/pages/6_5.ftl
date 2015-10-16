<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>选择支付方式</title>
	<#include "./inc/head.ftl">
	<style type="text/css">
		.QRCode iframe {
            -ms-zoom: 2.27;
            -moz-transform: scale(2.27);
            -moz-transform-origin: 0 0;
            -o-transform: scale(2.27);
            -o-transform-origin: 0 0;
            -webkit-transform: scale(2.27);
            -webkit-transform-origin: 0 0;
		}
	</style>
</head>
<body>
	<#include "./inc/header.ftl">
<div id="pc">
    <input type="hidden" id="ajax_paymentid" value="${paymentId}"/>
    <div class="payList">
        <h3 class="tt">
            支付方式</h3>
        <ul>
			<#if alipayForm?exists>
                <li>
					<#assign keys=alipayForm?keys>
                    <form action="https://mapi.alipay.com/gateway.do?_input_charset=${alipayForm["_input_charset"]}">
						<#list keys as key>
                            <input type="hidden" name="${key}" value="${alipayForm[key]}"/>
						</#list>
                        <input class="btn btnTaoBao" type="submit" value="支付宝网页支付"/>
                    </form>
                </li>
			</#if>
			<#if alipayQr?exists>
                <li class="payTaobao">
                    <div class="QRCode">
                        <iframe src="${alipayQr}" frameBorder=0 scrolling=no width="170" height="170"></iframe>
                    </div>
                    <div class="QRsm">
                        <img src="${requestContext.contextPath}/static/img/paytaobao.png"/>
                        <label>
                            支付宝扫码支付</label>
                    </div>
                </li>
			</#if>
			<#if wechat?exists>
                <li>
                    <div class="QRCode">
                        <img src="${requestContext.contextPath}${wechat}"/>
                    </div>
                    <div class="QRsm">
                        <img src="${requestContext.contextPath}/static/img/payweixin.png"/>
                        <label>
                            微信扫码支付</label>
                    </div>
                </li>
			</#if>
        </ul>
    </div>
</div>
	<#include "./inc/footer.ftl">
    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>