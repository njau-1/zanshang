<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>微信</title>
	<meta name="description" content="“赞赏”，中国第一社交出版平台，为“人人都赞赏成书”而创建。不“赞赏”，无思想。"/>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#include "./inc/header.ftl" />
    <div id="pc">
        <div id="media">
            <ul id="menu_about">
                <li><a href="${requestContext.contextPath}/support/about">关于赞赏</a></li>
                <li><a href="${requestContext.contextPath}/support/media">媒体报道</a></li>
                <li><a  href="${requestContext.contextPath}/support/join">加入我们</a></li>
                <li><a  href="${requestContext.contextPath}/support/faq">帮助中心</a></li>
                <li><a  class="cur">微信</a></li>
            </ul>
            <div id="new_about" >
            <div id="qrList">
                    <div id="server">
                        <h3>
                            服务号</h3>
                        <h4>
                            赞赏：zanshang2014</h4>
                        <div class="qrborder">
                            <img src="${requestContext.contextPath}/static/img/zsserver.png" />
                        </div>
                        <p>
                            “赞赏”，中国第一社交出版平台，为“人人都赞赏成书”而创建。不“赞赏”，无思想。
                        </p>
                    </div>
                    <div id="dy">
                        <h3>
                            订阅号</h3>
                        <h4>
                            帮你读书：gozanshang</h4>
                        <div class="qrborder">
                            <img src="${requestContext.contextPath}/static/img/zsdingyue.png" />
                        </div>
                        <p>
                            每天一个故事，一本好书。
                        </p>
                    </div>
                </div>
           </div>
    </div>
</body>
</html>
