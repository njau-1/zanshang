<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>参与的项目</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="orderList">
        <h3 class="tt">
            参与的项目</h3>
        <ul class="tab2">
            <li <#if tab == "orders">class="cur"</#if>><a
                    href="${requestContext.contextPath}/profile/orders">赞赏</a></li>
            <li <#if tab == "projects">class="cur"</#if>><a href="${requestContext.contextPath}/profile/projects">发起</a>
            </li>
            <li <#if tab == "published">class="cur"</#if>><a
                    href="${requestContext.contextPath}/profile/published">出版</a></li>
            <li></li>
        </ul>
        <div class="tabHide defaultShow">
		<#list content.getContent() as project>
            <div class="order<#if project.getState() == "failure"> fail</#if>">
                <div class="orderMsg">
                    <label>
					${project.getCreateTime()}</label>
                    <label>
						<#if tab="projects" || tab="published">项目：${project.getProjectId()}</#if>
						<#if tab="orders">单号：${project.getOrderId()}</#if>
					</label>
					<#if tab == "projects" && project.getFeedback()??>
                        <a class="remark">赞赏审核意见反馈
                            <p>
							${project.getFeedback()}</p>
                        </a>
					</#if>
                </div>
                <#if tab="projects">
                    <table class="table" cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="giftBook">
                                <#if project.getState() == "reviewing" ||  project.getState() == "pricing">
                                <a href="${requestContext.contextPath}/projects/${project.getProjectId()}/review">
                                    <img src="${imageRoot}${project.getCover()}?imageView2/2/w/60/h/90" class="shadow"/>
                                </a>
                                <#elseif project.getState() == "funding">
                                    <a href="${requestContext.contextPath}/projects/${project.getProjectId()}">
                                        <img src="${imageRoot}${project.getCover()}?imageView2/2/w/60/h/90" class="shadow"/>
                                    </a>
                                <#else>
                                    <img src="${imageRoot}${project.getCover()}?imageView2/2/w/60/h/90" class="shadow"/>
                                </#if>
                            </td>
                            <td width="120">
                                剩余<#if project.getDaysLeft()??>${project.getDaysLeft()}<#else >-</#if>天
                            </td>
                            <td>
                                筹款${project.getCurrentBalance()}/<#if project.getGoal()??>￥${project.getGoal()}<#else >
                                -</#if>
                            </td>
                            <td>
                                最低出版价<#if project.getFeedbackGoal()??>￥${project.getFeedbackGoal()}<#else >-</#if>
                            </td>
                            <td width="120px">
                                <a class="btnAudit">${project.getStrState()}</a>
                                <#if project.getState() == "pricing">
                                    <a href="${requestContext.contextPath}/projects/${project.getProjectId()}/pricing">设置回报</a>
                                </#if>
                                <#if project.getState() == "reviewing">
                                    <a href="${requestContext.contextPath}/projects/${project.getProjectId()}/review">编辑</a>
                                </#if>
                            </td>
                        </tr>
                    </table>
                </#if>
                <#if tab="published">
                    <table class="table" cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="giftBook">
                                <a href="${requestContext.contextPath}/projects/${project.getProjectId()}">
                                    <img src="${imageRoot}${project.getCover()}?imageView2/2/w/60/h/90" class="shadow"/>
                                </a>
                            </td>
                            <td width="120">
                                剩余<#if project.getDaysLeft()??>${project.getDaysLeft()}<#else >-</#if>天
                            </td>
                            <td>
                                筹款${project.getCurrentBalance()}/<#if project.getGoal()??>￥${project.getGoal()}<#else >
                                -</#if>
                            </td>
                            <td>
                                最低出版价<#if project.getFeedbackGoal()??>￥${project.getFeedbackGoal()}<#else >-</#if>
                            </td>
                            <td width="120px">
                                <a class="btnAudit">${project.getStrState()}</a>
                            </td>
                        </tr>
                    </table>
                </#if>
				<#if tab="orders">
                    <table class="table" cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="giftBook">
                                <a href="${requestContext.contextPath}/projects/${project.getProjectId()}">
                                	<img src="${imageRoot}${project.getCover()}?imageView2/2/w/60/h/90" class="shadow"/>
                                </a>
                            </td>
                            <td width="24%">
                                <ul class="giftList">
									<#assign items = project.getReward().getItems()>
									<#list items?keys as key >
										<#if key == "BOOK">
                                            <li>${items[key].getCount()} 本 ${project.getBookName()}</li></#if>
										<#if key == "VIP">
                                            <li>${items[key].getCount()} 张沙龙VIP</li></#if>
										<#if key == "OTHER">
                                            <li>${items[key].getDetail()}</li></#if>
									</#list>
                                </ul>
                            </td>
                            <!-- 这里帮我绑定一个点击“修改收货地址”跳转的相对页面地址addrurl和相应的地址id addrid，不能修改地址的订单，不要绑定这两个属性-->
                            <#if project.getState() == "publishing" ||  project.getState() == "funding">
                                <td class="td_addr" addrurl="7_7.htm?i=" addrid="${project.getOrderId()}">
                                <#if project.getAddress()?? &&  project.getAddress()?has_content>
                                    <p>${project.getAddress()}</p><a openUrl="#page7_11"><p style="color:#f00;cursor:pointer">修改收货地址</p></a>
                                <#else>
                                    <a openUrl="#page7_11"><p style="color:#f00;cursor:pointer">填写收货地址</p></a>
                                </#if>
                            <#else>
                            <td class="td_addr">
                                <#if project.getAddress()?? &&  project.getAddress()?has_content><p>${project.getAddress()}</p></#if>
                            </#if>
                            </td>
                            <td width="120px">
							${project.getCount()}份，￥${project.getPrice()}
                            </td>
                            <td width="120px">
							${project.getStrState()} <#if project.getState() == "funding">剩余${project.getDaysLeft()}天</#if>
                            </td>
                        </tr>
                    </table>
				</#if>
            </div>
		</#list>
            <div class="pager">
                <a
					<#if content.hasPrevious()>
                            class="prev"
                            href="${requestContext.contextPath}/profile/${tab}?page=${content.getNumber()-1}&size=${content.getSize()}"
					<#else >
                            class="prev noData"
					</#if>
                        >上一页</a><a
				<#if content.hasNext()>
                        class="next"
                        href="${requestContext.contextPath}/profile/${tab}?page=${content.getNumber()+1}&size=${content.getSize()}"
				<#else >
                        class="next noData"
				</#if>
                    >下一页</a>
            </div>
        </div>
    </div>
</div>
<div id="pf">
    <div id="copyright">
        COPYRIGHT © 2013-2015 Zanshang. ALL RIGHTS RESERVED<a>京ICP备13028457号-2</a></div>
</div>

<#if tab="orders">
<div class="win dn" id="page7_11">
    <div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                    修改收件地址
                </div>
                <ul id="addrList">
                    <#list addresses as address>
                        <li><label><input type="radio" name="addr" /><span>${address.getRecipient()}, ${address.getTelephone()}, ${address.getAddress()}
                            , ${address.getPostCode()}</span></label></li>
                    </#list>
                </ul>
                <div class="btnList">
                    <a class="btn" id="btn_editAddr">确认</a><a class="btn btn_addr" openUrl="#page7_7">增加新地址</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="win dn" id="page7_7">
    <div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                    添加新收件信息</div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" placeholder="姓名" class="txt" id="txtName_addr" />
                        </li>
                        <li>
                            <input type="text" placeholder="手机号码" class="txt" id="txtMob_addr" />
                        </li>
                        <li class="area">
                            <select id="sls_pro" name="sls_pro">
                                <option value="">省份</option>
                            </select>
                            <select id="sls_city" name="sls_city">
                                <option value="">城市</option>
                            </select>
                            <select id="sls_dist" name="sls_dist">
                                <option value="">区县</option>
                            </select>
                        </li>
                        <li>
                            <input type="text" placeholder="街道地址" class="txt" id="txtAddr" />
                        </li>
                        <li>
                            <input type="text" placeholder="邮编" class="txt number" id="txtPost" />
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                    <a class="btn btnDfAddr" id="btnSaveDefAddr2">确认同时设为默认地址</a><a class="btn" id="btnSaveAddr2">确定</a>
                </div>
            </div>
        </div>
    </div>
</div>
</#if>
    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/city.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>