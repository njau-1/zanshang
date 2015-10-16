﻿<#escape x as x?html>﻿
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>个人资料</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <div class="bigReg">
        <ul>
            <li>
                <label class="tt mg">
                    头像</label>
                <div class="rTools">
                    <img src="${imageRoot}${avatar}" id="uploadPhoto" />
                </div>
            </li>
            <li>
                <label class="tt">
                    笔名(出版时用做署名)</label>
                <span class="oldVal">${displayName}</span>
                <div class="rTools">
                    <a class="goEdit" href="${requestContext.contextPath}/mobile/settings/name"></a>
                </div>
            </li>
             <li>
                <label class="tt">
                    邮箱</label>
                <span class="sm <#if email?exists>oldVal</#if>">
                    <#if email?exists>
                    ${email}
                    <#else >
                        请输入你的邮箱
                    </#if>
                    </span>
                <div class="rTools">
                    <a class="goEdit" href="${requestContext.contextPath}/mobile/settings/email"></a>
                </div>
            </li>
             <li>
                <label class="tt">
                    密码</label>
                <span class="sm">******************</span>
                <div class="rTools">
                    <a class="goEdit" href="${requestContext.contextPath}/mobile/settings/password"></a>
                </div>
            </li>
        </ul>
    </div>
    <div class="bigReg">
        <ul>
            <li>
                <label class="tt">
                    姓名</label>
                <span class="sm">
                	<#if legalName??>${legalName}
                	<#else>请输入你的真实姓名以申请成为作者，审核认证后不可修改
                	</#if>
            	</span>
                <div class="rTools">
                    <a class="goEdit"></a>
                </div>
            </li>
             <li>
                <label class="tt">
                    手机号码</label>
                    <span class="sm <#if phone?exists>oldVal</#if>">
	                    <#if phone?exists>
	                    ${phone}
	                    <#else >
	                        请输入手机号码
	                    </#if>
                    </span>
                <div class="rTools">
                    <a class="goEdit" href="${requestContext.contextPath}/mobile/settings/phone"></a>
                </div>
            </li>
             <li>
                <label class="tt">
                    身份证号</label>
                <span class="sm">
	                <#if identityCode??>${identityCode}
	                <#else>请输入你的身份证号以申请成为作者，审核认证后不可修改
	                </#if>
                </span>
                <div class="rTools">
                    <a class="goEdit"></a>
                </div>
            </li>
             <li>
                <label class="tt mg">
                    身份证正反照</label>
                      <div class="rTools  uploadID">
                <a class="btnImg btnUpload">
                身份证正面
                	<#if identityFront??><img src="${imageRoot}${identityFront}"/></#if>
                </a>
                 <a class="btnImg btnUpload">
                 身份证背面
					<#if identityBack??><img src="${imageRoot}${identityBack}"/></#if>
                 </a>

                </div>
            </li>
             <li>
                <label class="tt">
                   收件地址</label>
                    <span class="sm <#if addresses?exists && addresses?size gt 0>oldVal</#if>">
                    <#if addresses?? && addresses?size gt 0>
	                    <#list addresses as address>
	                    ${address.getRecipient()}, ${address.getTelephone()}, ${address.getAddress()}
                            , ${address.getPostCode()}
		                    <#break >
	                    </#list>
                    <#else>
                        请完善你的收货地址，用于礼品邮寄
                    </#if>
                <div class="rTools">
                    <a class="goEdit"></a>
                </div>
            </li>
        </ul>
    </div>
    <div class="bigReg">
    <ul>
        <li>
                <label class="tt mg">
                    微信绑定</label>
                <div class="rTools">
					<#if wechat?? && wechat == false>
		                <a class="btn btnwx" href="${requestContext.contextPath}/wechat/unbind">
		                    <img src="${requestContext.contextPath}/static/img/weixin.png"/>已经绑定
		                </a>
					<#elseif wechat??>
		                <div class="weibo">
		                    <img src="${wechat_information.getAvatar()}"/><label>${wechat_information.getName()}&nbsp;</label><a href="${requestContext.contextPath}/wechat/unbind">解除绑定</a>
		                </div>
					<#else >
		                <a class="btn btnwx" href="https://open.weixin.qq.com/connect/qrconnect?appid=${wechat_appid}&redirect_uri=${wechat_redirect_uri}&response_type=code&scope=snsapi_login#wechat_redirect">
		                    <img src="${requestContext.contextPath}/static/img/weixin.png"/>绑定微信
		                </a>
					</#if>
                </div>
            </li>
            <li>
                <label class="tt mg">
                    微博绑定</label>
                <div class="rTools">
					<#if weibo?? && weibo == false>
	                    <a class="btn btnwx" href="${requestContext.contextPath}/weibo/unbind">
	                        <img src="${requestContext.contextPath}/static/img/weibo.png"/>已经绑定
	                    </a>
					<#elseif weibo??>
	                    <div class="weibo">
	                        <img src="${weibo_information.getAvatar()}"/><label>${weibo_information.getName()}&nbsp;</label><a href="${requestContext.contextPath}/weibo/unbind">解除绑定</a>
	                    </div>
					<#else >
	                    <a class="btn btnwx" href="https://api.weibo.com/oauth2/authorize?client_id=${weibo_appid}&response_type=code&redirect_uri=${weibo_redirect_uri}">
	                        <img src="${requestContext.contextPath}/static/img/weibo.png"/>绑定微博
	                    </a>
					</#if>
                </div>
            </li>
    </ul>
    </div>

    <div class="bigReg">
        <ul>
            <li>
                <a onclick="document.getElementById('logout').submit();" href="#">
                    <label class="tt mg">
                        退出登录
                    </label>
                </a>
            </li>
        </ul>
        <form action="${requestContext.contextPath}/authentication" method="post" id="logout">
            <input type="hidden" name="_method_" value="DELETE" />
        </form>
    </div
    <#assign menu_person=true>
    <#include "../inc/menu.ftl">
</body>
</html>
</#escape>
