﻿<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>沙龙详情-${projects[0].getBookName()}-${authorname}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <script>var server= "${requestContext.contextPath}/";</script>
</head>
<body>
<div id="shareTip">
    <label> 打开右上角微信分享</label>
</div>
<div style="display: none;">
    <img src="${imageRoot}${projects[0].getCover()}?imageView2/1/w/400/h/400"/>
</div>
	<div id="sharelayer">
		<div id="share">
			<div id="shareList">
				<#if isWechat?? && isWechat == true>
                    <a id="share_wx">&nbsp;</a>
				</#if>
				<a id="share_wb"
				   href="<@zanshang.shareWB shareLink "分享一个沙龙" />">&nbsp;</a>
				<a id="share_db"
                   href="<@zanshang.shareDB shareLink "分享一个沙龙" />">&nbsp;</a>
			</div>
			<div class="btnList">
				<a class="btn">取消</a>
			</div>
		</div>
	</div>
    <div id="bgBook">
        <div id="topAuthor">
		<#list projects as project>
            <table>
                <tr>
                    <td width="55">
                        <a href="${requestContext.contextPath}/profile/${project.getUid()}">
                        	<img src="${imageRoot}${avatar}" />
						</a>
                    </td>
                    <td>
                        <div id="topbook">
                            <div class="bookName">
                                《${project.getBookName()}》沙龙
                            </div>
                            <div id="bookAuthor">
								${authorname}
							</div>
                        </div>
                    </td>
                    <td width="40">
                        <a class="btn btnShare" id="btnshare"></a>
                    </td>
                </tr>
            </table>
			<#if project_has_next>
				<#break>
			</#if>
		</#list>
        </div>
        <div id="bookdoc">
			<#if currentTopic?exists>
	            <h4 class="tt">
					${currentTopic.getTitle()}</h4>
	            <div id="slTime"></div>
				<#if currentTopic.getImages()?size gt 0>
	            <div class="imgView">
					<#list currentTopic.getImages() as image>
                    	<img src="${imageRoot}${image}"/>
					</#list>
	            </div>
	            </#if>
	            <p class="pre">${currentTopic.getContent()}</p>
			<#else>
				<div class="empty">
					<h3>作者还没发出新样稿，每个样稿仅能存活24小时。</h3>
					<div class="btnList">
						<a class="btn" href="${requestContext.contextPath}/profile/${salon.getUid()}#pLMsg">私信作者</a>
					</div>
            	</div>
			</#if>
        </div>
    </div>
    <div id="pLMsg">
    	<ul class="msgList">
		<#if chats.getContent()?size gt 0>
			<#list chats.getContent() as chat>
				   <li <#if visitor?? && visitor.getUid() == chat.getMemberId()>class="my"</#if>>
                       <a href="${requestContext.contextPath}/profile/${chat.getMemberId()}"><img src="${imageRoot}${chat.getAvatar()}"/></a>
					   <div class="msgC">
						   <p>
						${chat.getChat()}
						   </p>

						   <div class="msgName">
						${chat.getName()}，<label><@zanshang.until chat.getCreateTime()/></label></div>
					   </div>
				   </li>
			</#list>
		<#else >
			   <div class="empty">发起沙龙里的第一句话吧…</div>
		</#if>
        </ul>
    </div>
	<#if isAuthor || isMember>
	   <div class="inputMsg">
		   <div>
			<img src="${imageRoot}${visitor.getAvatar()}"/>
			  <!--这里帮忙指定一个profileid-->
			   <textarea placeholder="输入聊天内容" id="txtMessage"></textarea>
		   </div>
		   <!--记得帮我绑定一下salon的id到按钮的salonid属性上-->
		   <a class="btn" id="btn_salonMsg" salonid="${salon.getUid()}">发送</a>
	   </div>
	</#if>
</body>
<script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/jquery.slides.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
</html>
</#escape>
