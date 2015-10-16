<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>赞赏回报选择</title>
	<#include "./inc/head.ftl">
</head>
<body>
<form action="" method="post">
    <div class="layer absolute">
        <div class="btnClose">
            &nbsp;</div>
        <div id="center">
            <#assign number=["壹","贰","叁","肆","伍","陆","柒","捌","玖","拾"]>
			<#list rewards as reward>
                <div class="bookSolu">
                    <div class="tt">
					${number[reward_index]}
                    </div>
                    <ul class="slnList">
						<#assign keys=reward.items?keys>
						<#list keys as key>
							<#if key == "BOOK">
                                <li>
                                    <img src="${imageRoot}${project.getCover()}?imageView2/2/w/90/h/130" class="shadow"/>

                                    <div class="bookCount">
                                        实体书×<label>${reward.items[key].getCount()}</label>
                                    </div>
                                </li>
							</#if>
							<#if key == "VIP">
                                <li>
                                    <img src="${requestContext.contextPath}/static/img/vip.png"/>

                                    <div class="bookCount">
                                        沙龙会员×<label>${reward.items[key].getCount()}</label>
                                    </div>
                                </li>
							</#if>
							<#if key == "SIGNATURE">
                                <li>
                                    <img src="${requestContext.contextPath}/static/img/sign.png"/>

                                    <div class="bookCount">
                                        作者签名×<label>${reward.items[key].getCount()}</label>
                                    </div>
                                </li>
							</#if>
							<#if key_has_next && keys[key_index+1] != "OTHER">
                                <li class="add">+</li>
							</#if>
						</#list>
                    </ul>
					<#if reward.items["OTHER"]?exists>
                        <div class="otherGift pre">${reward.items["OTHER"].getDetail()?html}</div>
					</#if>
                </div>
                <div class="btnList award">
					<#if state == "FUNDING">
                        <#if project.getId() == "55e65547e4b033b99cf284dd" && reward_index == 2>
                            <a class="btn"
                               onclick="alert('此方案已达限购名额，请赞赏其他方案！')">赞赏${reward.getPrice().to("YUAN")}
                                元</a>
                        <#else>
                        <a class="btn"
                           href="${requestContext.contextPath}/orders/creation?reward=${reward.getId()}">赞赏${reward.getPrice().to("YUAN")}
                            元</a>
                        </#if>
					<#elseif state == "REFUNDING"|| state == "FAILURE">
                        <a class="btn"
                           onclick="alert('本项目已失败，欢迎赞赏其他项目！')">赞赏${reward.getPrice().to("YUAN")}
                            元</a>
					<#elseif state == "SUCCESS" || state == "PUBLISHING" || state == "DELIVERING">
                        <a class="btn"
                           onclick="alert('本项目已成功，欢迎赞赏其他项目！')">赞赏${reward.getPrice().to("YUAN")}
                            元</a>
					</#if>
                </div>
			</#list>
        </div>
    </div>
</form>
    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>