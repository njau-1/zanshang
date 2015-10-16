<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>个人设置</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign menuControl=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="bigReg">
        <div id="uploadPhoto">
            <div class="btnUpload" id="btn_avatar" notip="true"><img src="${imageRoot}${avatar}"/></div>
        </div>
        <ul>
            <li>
                <label class="tt">
                    笔名(出版时用做署名)</label>
                <input type="text" class="txt" value="${displayName}" placeholder="${displayName}" id="txtName" disabled="disabled"/>
                <div class="rTools">
                    <a class="btnSmall btnEdit">修改</a>
                    <a class="btnSmall btnHide" id="set_Name">保存</a>
                    <a class="btnSmall btnCancel btnHide">取消</a>
                </div>
                <div class="error">笔名不能为空！</div>
            </li>
            <li>
                <label class="tt">
                    邮箱</label>
                <!--已添加过值，请设置class="oldVal",否则提示其填写的语句用class="sm",区别只是字体颜色不一样-->
                    <span class="sm <#if email?exists>oldVal</#if>">
                    <#if email?exists>
                    ${email}
                    <#else >
                        请输入你的邮箱
                    </#if>
                    </span>

                <div class="rTools">
                    <a class="btnSmall" openurl="#page7_4">
					<#if email?exists>
                        修改
					<#else>
                        绑定
					</#if>
                    </a>
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
                    <a class="btnSmall" openurl="#page7_3">
					<#if email?exists>
                        修改
					<#else>
                        绑定
					</#if>
                    </a>
                    
                </div>
            </li>
            <li>
                <label class="tt">
                    QQ号</label>
                <input type="text" class="txt number" value="<#if qq??>${qq?c}<#else >请输入你的QQ号码</#if>"" placeholder="<#if qq??>${qq?c}<#else >请输入你的QQ号码</#if>"
                       disabled="disabled" id="txtqq"/>

                <div class="rTools">
                    <a class="btnSmall btnEdit">
                        <#if qq??>
                            修改
                        <#else >
                            添加
                        </#if>
                    </a>
                    <a class="btnSmall btnHide" id="set_qq">保存</a>
                    <a class="btnSmall btnCancel btnHide">
                        取消</a>
                </div>
            </li>
            <li>
                <label class="tt">
                    姓名</label>
                <input type="text" class="txt"
                       placeholder="<#if legalName??>${legalName}<#else>输入真实姓名以认证为作者</#if>"
                       value="<#if legalName??>${legalName}</#if>"
                       disabled="disabled" id="txtlegalname"/>
			<#if !verified>
                <div class="rTools">
                    <a class="btnSmall btnEdit">
                        <#if legalName??>
                            修改
                        <#else >
                            添加
                        </#if>
                    </a>
                    <a class="btnSmall btnHide" id="set_legalname">保存</a>
                    <a class="btnSmall btnCancel btnHide">取消</a>
                </div>
			</#if>
            </li>
            <li>
                <label class="tt">
                    身份证号</label>
                <input type="text" class="txt"
                       placeholder="<#if identityCode??>${identityCode}<#else>输入真实身份证号以认证为作者</#if>"
                       value="<#if identityCode??>${identityCode}</#if>"
                       disabled="disabled" id="txtidentity"/>
			<#if !verified>
                <div class="rTools">
                    <a class="btnSmall btnEdit">
                        <#if identityCode??>
                            修改
                        <#else >
                            添加
                        </#if>
                    </a>
                    <a class="btnSmall btnHide" id="set_identity">保存</a>
                    <a class="btnSmall btnCancel btnHide">取消</a>
                </div>
			</#if>
            </li>
            <li>
                <label class="tt mg">
                    身份证正反照</label>

                <div class="rTools uploadID">
                    <a class="btnImg btnUpload" verified="${verified?c}" notip="true" id="id_front">身份证正面
					<#if identityFront??><img src="${imageRoot}${identityFront}"/></#if>
                    </a>
                    <a class="btnImg btnUpload" verified="${verified?c}" notip="true" id="id_back">身份证背面
					<#if identityBack??><img src="${imageRoot}${identityBack}"/></#if>
                    </a>
                    <a class="btnSmall btnHide">保存</a>
                    <a class="btnSmall btnHide">取消</a>
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
                        请完善你的收货地址
                    </#if>
                    </span>

                <div class="rTools">
                    <a class="btnSmall" openurl="#page7_6">
                        <#if identityCode??>
                            管理
                        <#else >
                            添加
                        </#if>
                    </a>
                    <!--     用户没有添加过收件地址时，openUrl应该直接指向7_7-->
                </div>
            </li>
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
                    <a class="btn btnwb" href="${requestContext.contextPath}/weibo/unbind">
                        <img src="${requestContext.contextPath}/static/img/weibo.png"/>已经绑定
                    </a>
				<#elseif weibo??>
                    <div class="weibo">
                        <img src="${weibo_information.getAvatar()}"/><label>${weibo_information.getName()}&nbsp;</label><a href="${requestContext.contextPath}/weibo/unbind">解除绑定</a>
                    </div>
				<#else >
                    <a class="btn btnwb" href="https://api.weibo.com/oauth2/authorize?client_id=${weibo_appid}&response_type=code&redirect_uri=${weibo_redirect_uri}">
                        <img src="${requestContext.contextPath}/static/img/weibo.png"/>绑定微博
                    </a>
				</#if>
                </div>
            </li>
            <li>
                <label class="tt">
                    密码</label>
                <span class="sm">******************</span>

                <div class="rTools">
                    <a class="btnSmall" openurl="#page7_5">修改</a>
                </div>
            </li>
        </ul>
    </div>
</div>
<#include "./inc/footer.ftl">
<div class="win dn" id="page7_3">
    <div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                    绑定手机
                </div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" placeholder="手机号码" class="txt number" id="txtMob"/>
                        </li>
                        <li>
                            <input type="text" placeholder="图片验证码" id="txtCaptcha" class="txt" maxlength="10" /><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image" /></a>
                        </li>
                        <li>
                            <input type="text" placeholder="短信验证码" class="txt" id="txtCode"/>
                            <input type="button" class="btnSmall btnVCode" value="获取"/>
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                    <a class="btn" id="set_personalPhone">确定</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="win dn" id="page7_4">
    <div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                    绑定邮箱
                </div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" placeholder="邮箱" class="txt" id="txtEmail"/>
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                    <a class="btn" id="set_email">发送验证邮件</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="win dn" id="page7_6">
    <div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="ttFont">
                收件信息管理
            </div>
            <ul class="addrList">
			<#list addresses as address>
                <li <#if address_index == 0>class="default" </#if>>
                    <label title="${address.getRecipient()}, ${address.getTelephone()}, ${address.getAddress()}, ${address.getPostCode()}">
					${address.getRecipient()}, ${address.getTelephone()}, ${address.getAddress()}
                        , ${address.getPostCode()}</label><a class="btn defAddr"
                                                             addressid="${address.getId()}">设为默认地址</a><a
                        class="btn del" addressid="${address.getId()}">删除</a></li>
			</#list>
            </ul>
            <div class="btnList">
                <a class="btn" openurl="#page7_7">添加新收件信息</a>
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
                            <input type="text" placeholder="街道地址" maxlength="100" class="txt" id="txtAddr" />
                        </li>
                        <li>
                            <input type="text" placeholder="邮编" class="txt number" id="txtPost" />
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
<div class="win dn" id="page7_5">
    <div class="layer">
        <div class="btnClose">
        </div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                    修改密码
                </div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="password" placeholder="原密码" class="txt" id="txtOldPwd"/>
                        </li>
                        <li>
                            <input type="password" placeholder="新密码" class="txt" id="txtNewPwd"/>
                        </li>
                        <li>
                            <input type="password" placeholder="确认新密码" class="txt" id="txtNewPwd2"/>
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                    <a class="btn" id="set_password">确定</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/city.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>

</body>
</html>
</#escape>