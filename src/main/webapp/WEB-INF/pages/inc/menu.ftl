<div id="open">&nbsp;</div>
<div id="rMenu">
    <ul >
    <#if visitor??>
        <li <#if menu_projects??>class="cur"</#if>><a href="${requestContext.contextPath}/projects">浏览</a></li>
        <li <#if menu_salon??>class="cur"</#if>><a href="${requestContext.contextPath}/salons">沙龙</a></li>
        <li <#if menu_join??>class="cur"</#if>><a href="${requestContext.contextPath}/profile/orders">订单</a></li>
        <li <#if menu_profile??>class="cur"</#if>><a href="${requestContext.contextPath}/profile/${visitor.getUid()}">个人</a></li>
        <li <#if menu_join??>class="cur"</#if>><a href="${requestContext.contextPath}/support/faq">帮助</a></li>
        <li <#if menu_person??>class="cur"</#if>><a href="${requestContext.contextPath}/settings">设置</a></li>
        <li class="slide"></li>
    <#else>
        <#if RequestParameters['return']??>
            <li <#if menu_authentication??>class="cur"</#if>><a href="${requestContext.contextPath}/authentication?return=${RequestParameters['return']}">登录</a></li>
            <li <#if menu_register??>class="cur"</#if>><a href="${requestContext.contextPath}/register?return=${RequestParameters['return']}">注册</a></li>
        <#elseif return??>
            <li <#if menu_authentication??>class="cur"</#if>><a href="${requestContext.contextPath}/authentication?return=${return}">登录</a></li>
            <li <#if menu_register??>class="cur"</#if>><a href="${requestContext.contextPath}/register?return=${return}">注册</a></li>
        <#else>
            <li <#if menu_authentication??>class="cur"</#if>><a href="${requestContext.contextPath}/authentication">登录</a></li>
            <li <#if menu_register??>class="cur"</#if>><a href="${requestContext.contextPath}/register">注册</a></li>
        </#if>
        <li <#if menu_projects??>class="cur"</#if>><a href="${requestContext.contextPath}/projects">浏览</a></li>
        <li <#if menu_salon??>class="cur"</#if>><a href="${requestContext.contextPath}/salons">沙龙</a></li>
        <li <#if menu_join??>class="cur"</#if>><a href="${requestContext.contextPath}/support/faq">帮助</a></li>
        <li class="slide"></li>
    </#if>
    </ul>
</div>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
