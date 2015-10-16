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
<div id="play">
    <div>如遇跨号支付问题，请长按识别二维码重试</div>
    <img src="${requestContext.contextPath}${wechat}"/>
</div>
<input type="hidden" id="ajax_paymentid" value="${paymentId}"/>
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script>
    var server = "${requestContext.contextPath}";
    statusAjax = function (paymentid) {
        /// <summary>支付状态轮询检查</summary>
        $.get(server + "/payments/" + paymentid + "/status", function (data) {
            if (data.code == "ok" && data.result.paid.toString() == "true") {
                clearInterval(stopAjax);
                window.location.href = server + data.result.callback;

            }
        }, "json").error(function () {
            clearInterval(stopAjax);
        });
    };
    stopAjax = setInterval(function () {
        statusAjax($("#ajax_paymentid").val());
    }, 1000);
</script>
</body>
</html>
</#escape>