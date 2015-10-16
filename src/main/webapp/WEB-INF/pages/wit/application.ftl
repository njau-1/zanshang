<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>填写您的信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/wit/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="page1" class="page">
    <div class="top">
        <img src="${requestContext.contextPath}/static/wit/img/tip_information.png" />
    </div>
    <ul id="reg">
        <li>
            <input type="text" placeholder="姓名" id="txt_name" class="test required" /></li>
        <li>
            <input type="text" placeholder="手机" id="txt_mob" class="test required mobile" /></li>
        <li>
            <input type="text" placeholder="邮箱" id="txt_email" class="test required email" /></li>
        <li>
            <input type="text" placeholder="职业" id="txt_job" class="test required" /></li>
    </ul>
    <div class="btn">
        <a id="btn_reg">
            <img src="${requestContext.contextPath}/static/wit/img/btn_next.png" /></a>
    </div>
</div>
<div id="page2" class="page">
    <div  class="top smallMg">
        <img src="${requestContext.contextPath}/static/wit/img/tip_choices.png" />
    </div>
<#list rewards as reward>
    <#if reward.getRewardId() == "55fb73c94568de9b26f086ce">
        <#assign first=reward >
    </#if>
    <#if reward.getRewardId() == "55fb73c94568de9b26f086cf">
        <#assign  second=reward >
    </#if>
</#list>
    <div class="div_tip" slnid="${first.getRewardId()}">
        <div class="top_tag">&nbsp;</div>
        <div class="tt">
            <span>¥${first.getPrice()}</span>
            <label>已购${first.getCount()-first.getCurrentCount()}人/限购${first.getCount()}人</label>
        </div>
        <ul>
            <li><span>1</span>成为&lt;WIT第1季&gt;现场观众；</li>
            <li><span>2</span>预先提问权（预购之后，即可在线留下您最想向3位预见家提出的问题，您的问题将会经由WIT团队整理后，在现场由相关讲者作答）；</li>
            <li><span>3</span>3位预见家在WIT活动现场披露的独家书单；</li>
            <li><span>4</span>WIT微信群的加入资格。</li>
        </ul>
        <div class="bottom_tag">&nbsp;</div>
    </div>
    <div class="div_tip" slnid="${second.getRewardId()}">
        <div class="top_tag">&nbsp;</div>
        <div class="tt">
            <span>¥${second.getPrice()}</span>
            <label>已购${second.getCount()-second.getCurrentCount()}人/限购${second.getCount()}人</label>
        </div>
        <ul class="blue">
            <li><span>1</span>包含¥${first.getPrice()}元所有项目；</li>
            <li><span>2</span>专场活动结束后，与3位预见家进行2小时私密茶叙。</li>

        </ul>
        <div class="bottom_tag">&nbsp;</div>
    </div>
    <div class="btn smallbtn">
        <a id="btn_confirm">
            <img src="${requestContext.contextPath}/static/wit/img/btn_confirm.png" alt="确认" />
        </a>
    </div>
</div>
<div id="page3" class="page">
    <form action="${requestContext.contextPath}/wit/application" method="post">
        <div class="top smallMg">
            <img src="${requestContext.contextPath}/static/wit/img/tip_confirmation.png" />
        </div>
        <div class="div_tip">
            <div class="top_tag">
                &nbsp;</div>
            <ul class="bigFont" id="regList">
                <li>姓名：<label>&nbsp;</label></li>
                <li>手机：<label>&nbsp;</label></li>
                <li>邮箱：<label>&nbsp;</label></li>
                <li>职业：<label>&nbsp;</label></li>
            </ul>
            <div class="bottom_tag">
                &nbsp;</div>
        </div>
        <div class="bigtt">您的回报方案</div>
        <div class="div_tip cur" id="sln_confirm">&nbsp;</div>
        <div class="btn smallbtn">
            <a id="link_play">
                <img src="${requestContext.contextPath}/static/wit/img/btn_play.png" alt="支付" />
            </a>
            <input type="submit" id="sbt_play" class="dn" />
            <!-- 姓名-->
            <input type="hidden" id="hd_name" name="username" />
            <!-- 手机-->
            <input type="hidden" id="hd_mob" name="phone" />
            <!-- 邮箱-->
            <input type="hidden" id="hd_email" name="email" />
            <!-- 职业-->
            <input type="hidden" id="hd_job" name="job" />
            <!--选择的方案-->
            <input type="hidden" id="hd_sln" name="rewardId" />

            <input type="hidden" id="hd_sln" name="projectId" value="55fb73c94568de9b26f086cd" />

            <input type="hidden" id="hd_sln" name="_method_" value="put" />
        </div>
    </form>
</div>
<script src="${requestContext.contextPath}/static/wit/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/wit/js/jquery.touchwipe.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/wit/js/test.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/wit/js/base.js" type="text/javascript"></script>
</body>
</html>
