<#escape x as x?html>﻿
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>参与的项目记录</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <script>var server = "${requestContext.contextPath}/";</script>
</head>
<body>
    <div id="pc">
        <ul class="tab">
            <li <#if tab == "orders">class="cur"</#if>><a href="${requestContext.contextPath}/profile/orders">赞赏</a></li>
            <li <#if tab == "projects">class="cur"</#if>><a href="${requestContext.contextPath}/profile/projects">发起</a>
            </li>
            <li <#if tab == "published">class="cur"</#if>><a href="${requestContext.contextPath}/profile/published">出版</a>
            </li>
        </ul>
		<#list content.getContent() as project>
			<div class="item">
				<div class="orderMsg">
					<label>
						${project.getCreateTime()}</label>
					</label>
					<span>
						<#if tab="projects" || tab="published">项目：${project.getProjectId()}</#if>
						<#if tab="orders">单号：${project.getOrderId()}</#if>
					</span>
				</div>
				<#if tab="orders">
		            <div class="topMsg">
		                <img src="${imageRoot}${project.getCover()}" class="bookpic shadow">
		                <ul class="zssln">
		                    <li>${project.getStrState()}, <#if project.getState() == "funding">剩余${project.getDaysLeft()}天</#if></li>
		                    <li>${project.getPrice()}元<span>×</span>${project.getCount()}份</li>
		                </ul>
		            </div>
		            <div class="giftList">
						<#assign items = project.getReward().getItems()>
		                <div class="slntt">
		                    <h5>
		                        将获赠</h5>
		                </div>
		                <div class="gift">
							<#list items?keys as key >
								<#if key == "BOOK">
                                    <a class="btn">${items[key].getCount()} 本 ${project.getBookName()}</a></#if>
								<#if key == "VIP">
                                    <a class="btn">${items[key].getCount()} 张沙龙VIP</a></#if>
								<#if key == "OTHER">
                                    <a class="btn">${items[key].getDetail()}</a></#if>
							</#list>
		                </div>
		            </div>
		            <div class="address">
						<#if project.getState() == "publishing" ||  project.getState() == "funding">
                            <#if project.getAddress()?? &&  project.getAddress()?has_content><label>${project.getAddress()}</label><a class="link_editAddr" addrid="${project.getOrderId()}">修改收货地址</a><#else><label></label><a class="link_editAddr" addrid="${project.getOrderId()}">填写收货地址</a></#if>
						<#else>
                            <label><#if project.getAddress()?? &&  project.getAddress()?has_content>${project.getAddress()}</#if></label>
						</#if>

                    </div>
				</#if>
				<#if tab="published">
                    <div class="topMsg">
                        <img src="${imageRoot}${project.getCover()}" class="bookpic shadow">
                        <ul class="zssln">
                            <li>${project.getStrState()}, <#if project.getState() == "funding">剩余${project.getDaysLeft()}天</#if></li>
                            <li>最低出版价&nbsp;￥<#if project.getFeedbackGoal()??>￥${project.getFeedbackGoal()}<#else >-</#if></li>
                            <li>筹款&nbsp;${project.getCurrentBalance()} / <label><#if project.getGoal()??>￥${project.getGoal()}<#else >
                                -</#if></label></li>
                        </ul>
                    </div>
				</#if>
				<#if tab="projects">
		            <div class="topMsg">
		                <img src="${imageRoot}${project.getCover()}" class="bookpic shadow">
		                <ul class="zssln">
		                    <li>${project.getStrState()}, <#if project.getState() == "funding">剩余${project.getDaysLeft()}天</#if></li>
		                    <li>最低出版价&nbsp;￥<#if project.getFeedbackGoal()??>￥${project.getFeedbackGoal()}<#else >-</#if></li>
		                    <li>筹款&nbsp;${project.getCurrentBalance()} / <label><#if project.getGoal()??>￥${project.getGoal()}<#else >
                                -</#if></label></li>
		                </ul>
		            </div>
				</#if>
        	</div>
		</#list>
        <div id="win">
            <div class="layer">
                <img src="${requestContext.contextPath}/static/mobile/img/btnclose.png" id="layer_close" alt="关闭窗口" />
                <div class="layerC">
                    <div class="layerBody">
                        <div class="ttFont">
                            收件地址
                        </div>
                        <ul id="addrList">
							<#list addresses as address>
                                <li><label><input type="radio" name="addr" /><span>${address.getRecipient()}, ${address.getTelephone()}, ${address.getAddress()}
                                    , ${address.getPostCode()}</span></label></li>
							</#list>
                        </ul>
                        <div class="btnList">
                            <a class="btn" id="btn_editMobAttr">确认</a><a class="btn btn_addr" id="btn_addMobAddr">增加新地址</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="win2">
            <div class="layer">
                <img src="${requestContext.contextPath}/static/mobile/img/btnclose.png" id="layer_close2" alt="关闭窗口" />
                <!-- 这个隐藏域要保留,我前端做标识用的-->
                <input type="hidden" id="isMob" value="1" />
                <div class="layerC">
                    <div class="layerBody">
                        <div class="ttFont">
                            添加新地址
                        </div>
                        <div class="reg">
                            <ul>
                                <li>
                                    <input type="text" placeholder="姓名" class="txt test required" id="txtName" />
                                </li>
                                <li>
                                    <input type="text" placeholder="手机号码" class="txt test required mobile" id="txtMob" />
                                </li>
                                <li>
                                    <input type="text" placeholder="详细地址" class="txt test required" id="txtAddr" />
                                </li>
                                <li>
                                    <input type="text" placeholder="邮编" class="txt test required zipCode" id="txtPost" />
                                </li>
                            </ul>
                        </div>
                        <div class="btnList">
                            <a class="btn btnDfAddr" id="btnSaveDefAddr">确认同时设为默认地址</a><a class="btn" id="btnSaveAddr">确定</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

		<#assign menu_join=true>
	<#include "../inc/menu.ftl">
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</html>
</#escape>

