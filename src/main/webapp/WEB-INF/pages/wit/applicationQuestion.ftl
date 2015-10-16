<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>填写提问</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/wit/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
<form action="${requestContext.contextPath}/wit/orders/${witOrderId}/qustion" method="post">
    <div id="h5">
        <div class="top">
            <img src="${requestContext.contextPath}/static/wit/img/tip_ask.png" />
        </div>
        <div id="div_answer">
            <h4>
                支付成功！</h4>
            <p>
                请在此填写您对3位讲者的提问内容，WIT团队将 进行整理，并在现场互动环节邀请讲者作答。
            </p>
            <div class="div_tip">
                <div class="top_tag">
                    &nbsp;</div>
                <textarea id="txtr_answer" name="question"></textarea>
                <div class="bottom_tag">
                    &nbsp;</div>
            </div>
        </div>
        <div class="btn">
            <input type="submit" id="sbt_answer" class="dn" />
            <input type="hidden" id="hd_witOrderId" name="witOrderId" value="${witOrderId}" />
            <a id="link_answer">
                <img src="${requestContext.contextPath}/static/wit/img/btn_submit.png" /></a>
        </div>
    </div>
    <script src="${requestContext.contextPath}/static/wit/js/jquery-1.4.1.min.js" type="text/javascript"></script>
    <script src="${requestContext.contextPath}/static/wit/js/jquery.touchwipe.min.js" type="text/javascript"></script>
    <script src="${requestContext.contextPath}/static/wit/js/test.js" type="text/javascript"></script>
    <script src="${requestContext.contextPath}/static/wit/js/base.js" type="text/javascript"></script>
</form>
</body>
</html>
</#escape>