<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>注册新用户</title>
    <#include "./inc/head.ftl">
</head>
<body>
<div class="layer">
    <div id="ph">
        <img src="${requestContext.contextPath}/static/img/zanshang.png" id="logo" />
        <ul id="topMenu">
            <li><a href="${requestContext.contextPath}/">首页</a></li>
            <li><a href="${requestContext.contextPath}/projects">浏览</a></li>
            <li><a href="${requestContext.contextPath}/salons">沙龙</a></li>
        </ul>
        <div id="msgRight">
            <#if RequestParameters['return']??>
                <a class="btn btnReg" href="${requestContext.contextPath}/authentication?return=${RequestParameters['return']}">登录</a>
            <#elseif return??>
                <a class="btn btnReg" href="${requestContext.contextPath}/authentication?return=${return}">登录</a>
            <#else>
                <a class="btn btnReg" href="${requestContext.contextPath}/authentication">登录</a>
            </#if>
        </div>
    </div>
    <div class="layerC">
        <div class="layerBody" style="width:453px">
            <ul class="tab" id="maxTab">
                <!-- 当前选中的选项卡请设一个class="cur"-->
                <li class="cur">个人手机</li>
                <li class="">个人邮箱</li>
                <li>机构</li>
            </ul>
            <div class="tabHide defaultShow">
                <@zanshang.bind "personalForm" />
                <form action="${requestContext.contextPath}/register/personal/phone" method="post">
                    <div class="reg reg1">
                        <ul>
                            <li>
                                <@zanshang.formInput "personalForm.phone" 'placeholder="手机" class="txt"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "personalForm.name" 'placeholder="笔名(出版时用做署名)" class="txt" maxlength="20" autocomplete="off"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formPasswordInput "personalForm.password" 'placeholder="密码" class="txt"' />
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <input type="password" id="confirmPassword" name="confirmPassword" value="" placeholder="确认密码"
                                       class="txt">
                            </li>
                            <li>
                                <input type="text" placeholder="图片验证码" id="txtCaptcha" class="txt" maxlength="10" autocomplete="off"><a id="yzm" href=""><img src="${requestContext.contextPath}/captcha/image"></a>
                            </li>
                            <li>
                                <@zanshang.formInput "personalForm.code" 'placeholder="验证码" name="code" class="txt test required"'/>
                                <@zanshang.showErrors "error" />
                                <span class="btn_getYzm">获取</span>
                            </li>
                        </ul>
                    </div>
                    <label class="lbFont" style="margin:12px 25px">
                        <input type="checkbox">阅读并同意<a target="_blank" href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>
                    <div class="btnList">
                        <#if RequestParameters['return']??>
                            <input type="hidden" name="return" id="return" value="${RequestParameters['return']}">
                        </#if>
                        <#if return??>
                            <input type="hidden" name="return" id="return" value="${return}">
                        </#if>
                        <input type="submit" class="btn" value="注册" id="btn_reg_pone">
                    </div>
                </form>
            </div>
            <div class="tabHide">
                <@zanshang.bind "personalForm" />
                <form action="${requestContext.contextPath}/register/personal/email" method="post">
                    <div class="reg reg1">
                        <ul>
                            <li>
                                <@zanshang.formInput "personalForm.email" 'placeholder="邮箱" class="txt"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "personalForm.name" 'placeholder="笔名(出版时用做署名)" class="txt" maxlength="20" autocomplete="off"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formPasswordInput "personalForm.password" 'placeholder="密码" class="txt"' />
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <input type="password" id="confirmPassword" value="" placeholder="确认密码" class="txt">
                            </li>
                        </ul>
                    </div>
                    <label class="lbFont" style="margin:12px 25px">
                        <input type="checkbox"/>阅读并同意<a target="_blank" href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>

                    <div class="btnList">
                        <#if RequestParameters['return']??>
                            <input type="hidden" name="return" id="return" value="${RequestParameters['return']}">
                        </#if>
                        <#if return??>
                            <input type="hidden" name="return" id="return" value="${return}">
                        </#if>
                        <input type="submit" class="btn" value="注册" id="btn_reg_pro"/>
                    </div>
                </form>
            </div>
            <div class="tabHide" id="div_organ">
                <@zanshang.bind "companyForm" />
                <form action="${requestContext.contextPath}/register/company" method="post">
                    <div class="reg reg1">
                        <ul>
                            <li>
                                <@zanshang.formInput "companyForm.companyName" 'placeholder="机构名称(正式全称)" class="txt"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "companyForm.companyCode" 'placeholder="组织机构代码" class="txt"'/>
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "companyForm.contact" 'placeholder="联系人姓名" class="txt"' />
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "companyForm.contactPhone" 'placeholder="联系人电话" class="txt"' />
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formInput "companyForm.email" 'placeholder="联系人邮箱(登录用邮箱)" class="txt"' />
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <@zanshang.formPasswordInput "companyForm.password" 'placeholder="密码" class="txt"' />
                                <@zanshang.showErrors "error" />
                            </li>
                            <li>
                                <input type="password" id="confirmPassword" value="" placeholder="确认密码" class="txt">
                            </li>
                            <li id="reg_license">
                                <input type="input" name="ph_license" placeholder="营业执照电子版" class="txt"
                                       disabled="disabled">
                                <a class="btnmg btnUpload" verified="false" notip="true" id="upload_license">点击上传</a>
                            </li>
                        </ul>
                    </div>
                    <label class="lbFont" style="margin:12px 25px">
                        <input type="checkbox"/>阅读并同意<a target="_blank" href="${requestContext.contextPath}/support/agreement">《赞赏注册服务协议》</a></label>

                    <div class="btnList">
                        <input type="submit" class="btn" value="注册"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
</body>
</html>
</#escape>