<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>提交赞赏订单</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="orderList">
        <h3 class="tt">
            订单详情</h3>
	<@zanshang.bind "order" />
        <form action="${requestContext.contextPath}/orders" method="post" id="form">
            <div class="order">
                <table class="table" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="giftBook">
                            <img src="${imageRoot}${project.getCover()}?imageView2/2/w/60/h/90" class="shadow"/>
                        </td>
                        <td class="giftwd">
                            <ul class="giftList">
							<#assign keys=reward.items?keys>
							<#list keys as key>
								<#if key == "BOOK">
                                    <li>
									${project.getBookName()}x${reward.items[key].getCount()}
                                    </li>
								</#if>
                                <#if key == "VIP">
                                    <li>
                                        VIP沙龙x${reward.items[key].getCount()}
                                    </li>
                                </#if>
                                <#if key == "SIGNATURE">
                                    <li>
                                        作者签名x${reward.items[key].getCount()}
                                    </li>
                                </#if>
								<#if key == "OTHER">
                                    <li>
									${reward.items[key].getDetail()}
                                    </li>
								</#if>
							</#list>
                            </ul>
                        </td>
                        <td>
                            <div class="zsCount">
                                赞赏
                                <div class="ckNum" min="1">
                                    <label>
                                        1
                                    </label>
                                    <a class="arrowUp"></a>
                                    <a class="arrowDown"></a>份
                                </div>
                            </div>
                        </td>
                        <td>
                            <label>
						        ${reward.getPrice().to("YUAN")?c}
                            </label>/元
                        </td>
                        <td>
                            免运费
                        </td>
                        <td>
                            印完后<label>10</label>天发货
                        </td>
                    </tr>
                </table>
            </div>
            <@zanshang.formHiddenInput "order.count"/>
            <div class="tellMe">
                <input type="text" name="comment" class="txt" maxlength="22" placeholder="您可以为作者送上一句鼓励的话语 (不超过20字)"/>
            </div>
            <#if isLogin?? && isLogin == false>
                <div id="orderEmail">
                    <input type="text" name="email" class="txt"  placeholder="您尚未登陆，请输入邮箱账号以便与您保持联系" />
                </div>
                <!-- 这一块html有修改，你对比一下6_4-->
                <div class="ckAddr menu" id="newAddr" style="display: none;">
                    <h5>
                        收件地址：</h5>
                    <label>
                    </label>
                    <a openurl="#page7_8" id="edit_addresss">修改</a>
                </div>
                <input type="hidden" name="address" id="address" value="" />
                <div class="btnRight">
                    <a class="btn" openurl="#page7_8">增加新地址</a>
                </div>
                <label class="lbFont" style="margin-bottom:25px">
                    <input type="checkbox" checked="" />阅读并同意<a target="_blank" href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a>
                </label>
            </#if>
            <#if isLogin?? && isLogin == true>
                <div class="ckAddr menu">
                    <a>
                        <h5>
                            收件地址：
                        </h5>
                        <#list addresses as address>
                            <#if address_index == 0>
                            <label>${address.getRecipient()},${address.getTelephone()},${address.getAddress()},${address.getPostCode()}</label><span class="arrowDown">&nbsp;</span>
                                <ul>
                            <#else >
                                <li>${address.getRecipient()},${address.getTelephone()},${address.getAddress()}
                                    ,${address.getPostCode()}</li>
                            </#if>
                            <#if !address_has_next>
                                </ul>
                            </#if>
                        </#list>
                    </a>
                </div>
                <@zanshang.formHiddenInput "order.address" "class='test address'"/>
                <div class="btnRight">
                    <a class="btn" href="${requestContext.contextPath}/settings/addresses?return=${(requestContext.contextPath+ "/orders/creation?reward="+ reward.getId())?url('utf-8')}">增加新地址</a>
                </div>
            </#if>
            <input type="hidden" name="rewardId" value="${reward.getId()}"/>
            <div class="btnList" style="margin-top:0"><a class="btn" id="btnOrder">合计<label>${reward.getPrice().to("YUAN")?c}</label>元，提交订单</a></div>

    </div>
</div>
</form>
<!--  未登录的用户弹层,6_4中的id为page7_7的是已登录用户的弹层，如果你放一个页面，就把这两个弹层拷到一起吧-->
<div class="win dn" id="page7_8">
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
                            <input type="text" placeholder="街道地址" maxlength="100" class="txt" id="txtAddr" />
                        </li>
                        <li>
                            <input type="text" placeholder="邮编" class="txt number" id="txtPost" />
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                    <!-- 这一块html有修改-->
                    <a class="btn" id="btnNewAddr">确定</a>
                </div>
            </div>
        </div>
    </div>
</div>

</div>
<#include "./inc/footer.ftl">
    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<link href="${requestContext.contextPath}/static/module/layer/skin/layer.css" rel="stylesheet" type="text/css"/>
<link href="${requestContext.contextPath}/static/module/layer/skin/layer.ext.css" rel="stylesheet" type="text/css"/>
<script src="${requestContext.contextPath}/static/module/layer/layer.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/city.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/test.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>