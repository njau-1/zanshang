<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="apple-touch-fullscreen" content="YES">
	<meta name="format-detection" content="telephone=no">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black"><meta http-equiv="Expires" content="-1">
	<meta http-equiv="pragram" content="no-cache">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" user-scalable="no">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>答案</title>
	<link href="/static/market/heiwq/css/answer.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="wrapper answer">
		<img src="${requestContext.contextPath}/static/market/heiwq/images/share_text.png" class="share">
		<img src="${requestContext.contextPath}/static/market/heiwq/images/2.gif" class="img_gif" id="img_gif">
		<img src="${requestContext.contextPath}/static/market/heiwq/images/a1.png" class="answer_con" id="answer_con">
		<a href="http://www.zan-shang.com"><img src="${requestContext.contextPath}/static/market/heiwq/images/a_bottom.png" class="a_bottom"></a>
	</div>

<script src="${requestContext.contextPath}/static/market/heiwq/zepto.min.js" type="text/javascript"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	//把传过来的答案字符串放到b中
	var answer="CABADDCACC",
		b="${RequestParameters.answer}",
		c=0;
	for(i=0;i<10;i++){
		if(answer.substr(i,1)==b.substr(i,1)){
			c++;
		}
	}

    var img_gif;
    var ans_con;
    if (c < 3) {
        img_gif = __uri('/static/market/heiwq/images/1.gif');
        ans_con = __uri('/static/market/heiwq/images/a1.png');
    } else if (c < 5) {
        img_gif = __uri('/static/market/heiwq/images/2.gif');
        ans_con = __uri('/static/market/heiwq/images/a2.png');
    } else if (c < 7) {
        img_gif = __uri('/static/market/heiwq/images/3.gif');
        ans_con = __uri('/static/market/heiwq/images/a3.png');
    } else if (c < 9) {
        img_gif = __uri('/static/market/heiwq/images/4.gif');
        ans_con = __uri('/static/market/heiwq/images/a4.png');
    } else if (c < 11) {
        img_gif = __uri('/static/market/heiwq/images/5.gif');
        ans_con = __uri('/static/market/heiwq/images/a5.png');
    }
    document.getElementById("img_gif").src = img_gif;
    document.getElementById("answer_con").src = ans_con;

</script>
<script>
    // 注意：所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
    // 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
    // 完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	<#assign keys=config?keys>
    wx.config({
	<#list keys as key>
	${key}:'${config[key]}',
	</#list>
            jsApiList:
    [
        // 所有要调用的 API 都要加到这个列表中
        'checkJsApi',
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo',
    ]
    })
    ;
    wx.ready(function () {
        // 在这里调用 API
        var shareData = {
            title: "HEY！ 文青",//标题
            desc: "文青？伪文青？点开才知道。",//描述
            link: "http://www.zan-shang.com/marketing/h5/heywq/index", //地址
            imgUrl: __uri('/static/market/heiwq/images/share_img.jpg') //缩略图
        };
        wx.onMenuShareAppMessage(shareData);
        wx.onMenuShareTimeline(shareData);
        wx.onMenuShareQQ(shareData);
        wx.onMenuShareWeibo(shareData);
    });
</script>
</body>
</html>