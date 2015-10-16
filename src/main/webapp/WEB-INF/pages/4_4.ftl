<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>发表沙龙话题</title>
	<#include "./inc/head.ftl">
</head>
<body>
<div class="layer">
    <div class="layerC">
        <form action="${requestContext.contextPath}/salons/${id}/topic" method="post">
            <div class="layerBody">
                <div class="ttFont">
                    发表样稿
                </div>
                <textarea placeholder="#请输入你的样稿标题#" id="title" name="title" class="topic" max-height="100"></textarea>
                <div class="uploadImgList">
                    <div class="img1 btnUpload">
                        <span>相关配图</span>
                    </div>
                    <div class="img2 btnUpload">
                        <span>相关配图</span>
                    </div>
                    <div class="img3 btnUpload">
                        <span>相关配图</span>
                    </div>
                </div>
                <textarea placeholder="相关话题内容 ..." id="content" name="content" class="topic" max-height="200"></textarea>
                <div class="btnList">
                    <input type="submit" id="sbtTopic" class="dn"/>
                    <a class="btn" id="btnTopic">发表</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
</body>
</html>
</#escape>