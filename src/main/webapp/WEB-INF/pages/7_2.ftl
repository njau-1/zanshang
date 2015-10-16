<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>机构设置</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="bigReg">
        <div id="uploadPhoto" class="mg">
            <div class="btnUpload" id="btn_avatar" notip="true"><img src="${imageRoot}${avatar}"/></div>
				<#if !verified>
            		<a class="btn" id="set_publisherApply">申请出版商</a>
				</#if>
        </div>
        <ul>
            <li>
                <label class="tt">
                    机构名称(正式全称)</label>
                <!--已添加过值，请设置class="oldVal",否则提示其填写的语句用class="sm",区别只是字体颜色不一样-->
                <span class="oldVal">${companyName}</span>
				<#if verified>
					<span class="small">（已认证为出版人）</span>
				</#if>

				<#if !verified>
	                <div class="rTools">
	                    <a class="btnSmall btnEdit">修改</a>
	                    <a class="btnSmall btnHide" id="set_Name">保存</a>
	                    <a class="btnSmall btnCancel btnHide">取消</a>
	                </div>
	                <div class="error">用户名不能为空！</div>
				</#if>
            </li>
            <li>
                <label class="tt">
                    机构邮箱</label>
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
                    组织机构代码</label>
                <input type="text" class="txt" id="txtCode" disabled="disabled" value="${companyCode}"/>
			<#--<#if !verified>-->
                <#--<div class="rTools">-->
                    <#--<a class="btnSmall btnEdit">修改</a>-->
                    <#--<a class="btnSmall btnHide" id="set_code">保存</a>-->
                    <#--<a class="btnSmall btnCancel btnHide">取消</a>-->
                <#--</div>-->
			<#--</#if>-->
            </li>
            <li>
                <label class="tt mg">
                    营业照复印件</label>

                <div class="rTools  uploadID">
                    <!--verified="true"表示不能再上传了-->
                    <a class="btnImg btnUpload" verified="true" notip="true" id="set_license">营业照复印件
                        <img src="${imageRoot}${license}"/>
                            <div class="tip">
                                审核通过
                            </div>
                    </a>
                </div>

            </li>
            <li>
                <label class="tt">
                    联系人</label>
                <input type="text" class="txt" placeholder="${displayName}" id="txtName" disabled="disabled"/>

            <#if !verified>
                <div class="rTools">
                    <a class="btnSmall btnEdit">修改</a>
                    <a class="btnSmall btnHide" id="set_Name">保存</a>
                    <a class="btnSmall btnCancel btnHide">取消</a>
                </div>
            </#if>
            </li>

            <li>
                <label class="tt">
                    联系人手机号码</label>
                <input type="text" class="txt number" placeholder="${contactPhone}"/>

                <div class="rTools">
                <#if contactPhone??>
                    <a class="btnSmall" openurl="#page7_3">修改</a>
                <#else>
                    <a class="btnSmall" openurl="#page7_3">绑定</a>
                </#if>
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
                        <#if addresses??>
                            管理
                        <#else >
                            添加
                        </#if>
                    </a>
                    <!--     用户没有添加过收件地址时，openUrl应该直接指向7_7-->
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
<!--    隐藏的弹层-->
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
                            <input type="text" placeholder="短信验证码" class="txt" id="Text1"/>
                            <input type="button" class="btnSmall btnVCode" value="获取"/>
                        </li>
                    </ul>
                </div>
                <div class="btnList">
                    <a class="btn" id="set_companyPhone">确定</a>
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

                    <div class="msgFont">
                        验证邮件已发出，请查收并点击邮件中的链接
                    </div>
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