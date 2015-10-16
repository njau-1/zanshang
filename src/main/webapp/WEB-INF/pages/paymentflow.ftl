<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>支付宝网银U盾付款流程演示</title>
    <meta name="description" content="“赞赏”，中国第一社交出版平台，为“人人都赞赏成书”而创建。不“赞赏”，无思想。"/><meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <#include "./inc/head.ftl">
    <style type="text/css">
        .wrap {
            margin: 80px auto;
            width: 100%;
        }
        .content {
            width: 900px;
            margin: 20px auto;
            text-align: center;
        }
        .content h3{
            margin: 10px 0;
            font-size: 26px;
        }
        .content p {
            font-size: 20px;
            margin: 30px 0;
        }
        .content .red {
            color: red;
        }
        .content img {
            width: 900px;
        }
    </style>
</head>
<body>
<#assign border=true>
<#include "./inc/header.ftl" />
<div class="wrap">
    <div class="content" >
        <h3>支付宝网银U盾付款流程演示</h3>
        <hr>
        <p>当赞赏的回报价格超过<span class="red">3000</span>元时，请使用支付宝中的网银并配合U盾进行付款，流程如下图。</p>
        <p>1.下单：如果您是非登录状态，请记得填写邮箱；如果是已登录状态，请选择或新添一个收件地址。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow0.jpg"></p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow1.jpg"></p>
        <p>2.选择支付宝网页支付。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow2.jpg"></p>
        <p>3.登录支付宝，请注意是通过支付密码而不是登录密码。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow3.jpg"></p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow4.jpg"></p>
        <p>4.登录支付宝账号后在收银台中点击红圈中的“+ 银行卡”按钮。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow5.jpg"></p>
        <p>5.在浮窗中输入银行卡号，点击“下一步”按钮。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow6.jpg"></p>
        <p>6.选择红圈中的“网上银行（需开通网银）”付款方式，下方有对应银行的U盾支付限额。点击“下一步”。此时请将U盾插到电脑上。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow7.jpg"></p>
        <p>7.在下一步中点击红圈中的“登录到网上银行付款”按钮，进入网银付款页面。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow8.jpg"></p>
        <p>8.在网银页面中完成支付，请确保您的U盾在整个过程中都插在电脑USB口上。（以下截图以工行网银为例）</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow9.jpg"></p>
        <p>9.网银支付成功以后，您就完成了对作者的赞赏支持。</p>
        <p><img src="${requestContext.contextPath}/static/img/paymentflow10.jpg"></p>
    </div>
</body>
</html>
