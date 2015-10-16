﻿<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>提交赞赏订单</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        #link_buy
        {
            float:right;
            color:#3f88ec;
            font-size:14px;
            position:relative;
            top:2px;
        }
        .giftList  .slntt h5
        {
            float:left;
        }
        .slntt
        {
            margin-bottom:10px;
        }
    </style>
    <script>var server = "${requestContext.contextPath}/";</script>
</head>
<body>
	<@zanshang.bind "order" />
    <form action="${requestContext.contextPath}/orders" method="post" id="form">
    <div class="bg">
        <div class="topsln">
            <div class="pc">
                <h4 class="tt">
                    赞赏方案</h4>
                <img src="${imageRoot}${project.getCover()}" class="bookpic" />
                <ul class="zssln">
                    <li class="bookName">${project.getBookName()}</li>
                    <li class="bookAuthor">${authorDisplayName}</li>
                    <li class="addCount">
                        <label>
                            赞赏</label>
                        <a class="jian">-</a>
                            <span id="orderCount"><label>1</label>份</span>
                        <a class="jia">+</a> <input type="hidden" name="count" id="hd_count" value="1" />
                    </li>
                    <li id="orderprice">
                        <label>${reward.getPrice().to("YUAN")?c}</label>/ 元
                    </li>

                </ul>
            </div>
        </div>
        <div class="giftList">
            <div class="slntt">
                <h5>将获赠</h5>
                <#if isLogin?? && isLogin == false>
                    <a href="${requestContext.contextPath}/authentication" id="link_buy">我要登录</a>
                </#if>
            </div>
            <div class="gift">
				<#assign keys=reward.items?keys>
				<#list keys as key>
					<#if key == "BOOK">
                		<a class="btn">${reward.items[key].getCount()}本《${project.getBookName()}》</a>
					</#if>
                    <#if key == "VIP">
                        <a class="btn">${reward.items[key].getCount()}个沙龙VIP</a>
                    </#if>
                    <#if key == "SIGNATURE">
                        <a class="btn">${reward.items[key].getCount()}个作者签名</a>
                    </#if>
					<#if key == "OTHER">
	                	<a class="btn">${reward.items[key].getDetail()}</a>
					</#if>
				</#list>
            </div>
        </div>
        <div class="bigReg">
            <ul>
                <li <#if isLogin?? && isLogin == false> id="crt_addEmail"</#if>>
                    <label class="tt">
                        收件人地址</label>
                        <#if isLogin?? && isLogin == false>
                            <div type="text"  class="oldVal" disabled="disabled" style=" background-color:#fff; border-width:0;color:#5c5c5c;width:90%"></div>
                            <div class="rTools">
                                <a class="goEdit"></a>
                            </div>
                        <#else>
                            <a href="${requestContext.contextPath}/settings/addresses?return=${(requestContext.contextPath+ "/orders/creation?reward="+ reward.getId())?url('utf-8')}">
                                <#list addresses as address>
                                    <#if address_index == 0>
                                        <div type="text"  class="oldVal" disabled="disabled" style=" background-color:#fff; border-width:0;color:#5c5c5c;width:90%">${address.getRecipient()},${address.getTelephone()},${address.getAddress()},${address.getPostCode()}</div>
                                    </#if>
                                </#list>
                                <div class="rTools">
                                    <span class="goEdit"></span>
                                </div>
                            </a>
                        </#if>
            	<@zanshang.formHiddenInput "order.address" "class='test address'"/>
                </li>
                <#if isLogin?? && isLogin == false>
                    <li>
                        <input type="text" name="phone" placeholder="手机号(用作赞赏账户)" class="txt test required mobile pay-st" id="txtMob" />
                    </li>
                    <li>
                        <input type="text" name="captcha" placeholder="短信验证码" class="txt test required pay-st" />
                        <input style="top:17px" class="btnSmall btnVCode1"
                               type="button" value="获取" />
                    </li>
                    <label class="lbFont" style="margin: 12px 30px;"><input checked="" class="test checked " type="checkbox" />阅读并同意<a href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                </#if>
            </ul>
            <input type="hidden" name="rewardId" value="${reward.getId()}"/>
            <div class="btnList">
                <a id="payment" class="btn btnplay btntest">去支付</a>
            </div>

        </div>
    </div>
    <div id="tip_font">
        <ul id="ul_font">
            <li>大额支付请到电脑浏览器中打开您收到的项目链接下单，并选择使用支付宝网页-网银U盾支付。</li>
            <li style="margin-top: 20px;">使用方法：</li>
            <li>1.在电脑浏览器中打开下面的网址；
                <label class="link">
                ${tinyurl}</label>
            </li>
            <li>2.下单付款时选择“支付宝网页支付”；</li>
            <li>3.支付宝网银U盾使用入口详见电脑版的流程演示。</li>
        </ul>
        <a class="link" id="useLink" onclick="javascript: $('#sbt_goPlay').click()">继续手机支付></a>
        <input type="submit" id="sbt_goPlay" class="dn" />
    </div>
    <#if isLogin?? && isLogin == false>
    <div id="win">
        <div class="layer">
            <img src="${requestContext.contextPath}/static/mobile/img/btnclose.png" id="layer_close" alt="关闭窗口" />
            <div class="layerC">
                <div class="layerBody">
                    <div class="reg">
                        <ul>
                            <li>
                                <input type="text" placeholder="姓名" class="txt test required" id="txtName" />
                            </li>
                            <li>
                                <input type="text" placeholder="手机号码" class="txt test required mobile" id="txtMobile" />
                            </li>
                            <li>
                                <input type="text" placeholder="详细地址，如北京市X区X路X号" maxlength="100" class="txt test required" id="txtAddr" />
                            </li>
                            <li>
                                <input type="text" placeholder="邮编" class="txt test required zipCode" id="txtPost" />
                            </li>
                        </ul>
                    </div>
                    <div class="btnList">
                        <a class="btn" id="btnAddShowEmail">确定</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </#if>
    </form>


</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<link href="${requestContext.contextPath}/static/module/layer.m/need/layer.css" rel="stylesheet" type="text/css"/>
<script src="${requestContext.contextPath}/static/module/layer.m/layer.m.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</html>
</#escape>