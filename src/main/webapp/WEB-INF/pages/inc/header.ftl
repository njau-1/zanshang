<div id="ph" <#if border??>class="border"</#if>>
    <a href="${requestContext.contextPath}/"> <img src="${requestContext.contextPath}/static/img/logo.png" id="logo"/></a>
    <ul id="topMenu">
        <li><a href="${requestContext.contextPath}/">首页</a></li>
        <li><a href="${requestContext.contextPath}/projects" <#if header_projects??>class="cur"</#if>>浏览</a></li>
        <li><a href="${requestContext.contextPath}/salons" <#if header_salon??>class="cur"</#if>>沙龙</a></li>
    </ul>
    <ul id="msgRight">
        <script>var server = "${requestContext.contextPath}/";</script>
	<#if visitor??>
        <li class="btnIcon">
			<#if hasNews?? && hasNews == true>
                <div class="msg">&nbsp;</div></#if>
            <a href="${requestContext.contextPath}/notifications"><img src="${requestContext.contextPath}/static/img/msg.png"/>
            </a>
        </li>
        <li class="btnIcon">
            <a href="${requestContext.contextPath}/profile/orders"> <img
                    src="${requestContext.contextPath}/static/img/items.png" class="items"/></a>
        </li>
        <li id="userPhoto">
            <img src="${imageRoot}${visitor.getAvatar()}"/>
			<div id="menuControl">
	            <div id="hoverMenu" class="shadow">
	                <div class="arrowUp">
	                    &nbsp;</div>
	                <ul>
						<#if isPerson?? && isPerson == true>
	                        <li><a href="${requestContext.contextPath}/projects/creation">我要出版</a></li>
						</#if>
	                    <li><a href="${requestContext.contextPath}/profile/${visitor.getUid()}">个人主页</a></li>
	                    <li><a href="${requestContext.contextPath}/settings">个人设置</a></li>
	                    <li><a onclick="document.getElementById('logout').submit();" href="#">退出</a></li>
	                </ul>
	            </div>
    		</div>
        </li>
		<form action="${requestContext.contextPath}/authentication" method="post" id="logout">
			<input type="hidden" name="_method_" value="DELETE" />
		</form>
	<#else>
        <a class="btn btnlogo" href="${requestContext.contextPath}/authentication">登录/注册</a>
	</#if>
    </ul>
</div>