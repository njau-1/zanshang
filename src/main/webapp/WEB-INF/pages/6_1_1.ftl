<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>项目详情</title>
    <meta name="description"
          content="<#if project.getDescription()??>${project.getDescription()}<#elseif isPreview == true>赞赏图书预览</#if>"/>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div id="bookCover">
        <div id="blackLayer">&nbsp;</div>
        <#if isAuthor?? && isAuthor == true>
        <a class="btn" id="btnEdit" projectid="${project.getProjectId()}">编辑</a>
	</#if>
        <img src="${imageRoot}<#if project.getCover()??>${project.getCover()}?imageView2/2/w/152/h/225<#else>/@/static/default_cover.png</#if>" class="blur"/>

        <div id="topBookMsg">
                <span class="state">
                <#switch project.getState()>
	                <#case "FUNDING">
                        进行中
		                <#break >
	                <#case "PUBLISHING">
                        出版中
		                <#break >
	                <#case "DELIVERING">
                        已发货
		                <#break >
	                <#case "SUCCESS">
                        已成功
		                <#break >
	                <#case "REFUNDING">
                        退款中
		                <#break >
                    <#case "FAILURE">
                        已失败
                        <#break >
                    <#case "REVIEWING">
                        预览
                        <#break >
                </#switch>
                </span>
            <img src="${imageRoot}<#if project.getCover()?has_content>${project.getCover()}?imageView2/2/w/152/h/225<#else>/static/default_cover.png</#if>" class="shadow"/>

            <div class="bookName">
			    <#if project.getBookName()?has_content>${project.getBookName()}<#elseif isPreview == true>未填写书名</#if></div>
            <div class="bookAuthor">
                <#if project.getAuthorName()?has_content>${project.getAuthorName()}<#elseif isPreview == true>未填写作者名</#if></div>
            <ul class="bookState">
                <#if isPreview == false>
                <li <#if project.getCurrentMoney() gte project.getGoal()>class="suc"</#if>>
                    <div class="progress">
					${(project.getCurrentMoney()/project.getGoal() * 100)?floor}%
                    </div>
                    <div>
                        已筹
                    </div>
                </li>
                <li <#if project.isDeadline()>class="suc"</#if>>
                    <div class="progress">
					${project.getPassedDays()} / ${project.getTotalDays()}
                    </div>
                    <div>
                        进行天数
                    </div>
                </li>
                <li <#if project.isPublisherLocked()>class="suc"</#if>>
                    <div class="progress">
                        /
                    </div>
                    <div>
                        出版支持
                    </div>
                </li>
                <#else>
                    <li>
                        <div class="progress">
                            0%
                        </div>
                        <div>
                            已筹
                        </div>
                    </li>
                    <li>
                        <div class="progress">
                        0 / 0
                        </div>
                        <div>
                            进行天数
                        </div>
                    </li>
                    <li>
                        <div class="progress">
                            /
                        </div>
                        <div>
                            出版支持
                        </div>
                    </li>
                </#if>
            </ul>
        </div>
    </div>
    <div id="bookDetails">
        <div class="noEdit">
            <h4 class="tt">
                关于书</h4>
            <p class="pre"><#if project.getDescription()?has_content>${project.getDescription()}<#elseif isPreview == true>此处为图书描述</#if></p>
		<#if project.getTags()?has_content>
            <center>
                <ul class="bookTagList">

					<#list project.getTags() as tag>
                        <li><a class="tag btn">${tag}</a></li>
					</#list>
                </ul>
            </center>
		</#if>
        </div>
	<#if project.getImages()?has_content>

        <div class="picView imgView" style="display:block;">
            <#list project.getImages() as image>
                <li>
                    <img src="${imageRoot}${image}?imageView2/2/w/650/h/340"/>
                </li>
            </#list>
        </div>

	</#if>
        <div id="editPicView">
            <!-- 这一块我默认隐藏是点编辑按钮才显示的，但你绑定数据的时候，把图片也绑定上去-->
            <div class="uploadImgList">
            <#if project.getImages()?has_content>
	            <#list project.getImages() as image>
                    <div class="img${image_index+1} btnUpload">
                        <img src="${imageRoot}${image}"/>
                        <span>相关配图</span>
                        <div class="imgClose" title="删除图片">&nbsp;</div>
                    </div>
	            </#list>
	            <#assign size=(project.getImages()?size)>
            <#else>
                <#assign size=0>
            </#if>
	            <#if size < 3>
		            <#list size+1 ..3 as i>
	                    <div class="img${i} btnUpload">
	                        <span>相关配图</span>
	                    </div>
		            </#list>
	            </#if>
            </div>
        </div>
        <div class="noEdit">
            <h4 class="tt">
                关于我</h4>
		<#list authors as author>
            <div class="zsAuthor">
                <a <#if isPreview == false>href="${requestContext.contextPath}/profile/${author.getUid()}"</#if>>
                    <img src="${imageRoot}<#if isPreview == false>${author.getAvatar()}<#elseif isPreview == true && author_index == 0>${visitor.getAvatar()}<#elseif isPreview == true>/static/random${author_index}.png</#if>"/>
                </a>
                <label>
                    <#if author.getName()?has_content>${author.getName()}<#elseif isPreview == true>此处为作者名字</#if>
				</label>
            </div>
            <p class="pre"><#if author.getDescription()?has_content>${author.getDescription()}<#elseif isPreview == true>此处为作者描述</#if></p>
		</#list>
        <#if project.getOutline()?has_content>
            <h4 class="tt">
                目录大纲</h4>

            <div class="catalog">
                <p class="pre">${project.getOutline()}</p>
            </div>
        <#elseif isPreview == true>
            <h4 class="tt">
                目录大纲</h4>

            <div class="catalog">
                <p class="pre">此处为目录大纲</p>
            </div>
        </#if>
        </div>
        <div class="noEdit">
            <div class="btnTools">
                <div class="award">
                    <a class="btn" <#if isPreview == false>
                       href="${requestContext.contextPath}/projects/${project.getProjectId()}/rewards"</#if>>去赞赏</a>
                </div>
                <ul class="rightTools">
                    <li>
                        <div class="shareMenu">
                            <ul class="shadow">
                                <li><a class="sweibo" target="_blank"
                                       <#if isPreview == false>href="<@zanshang.shareWB "${shareLink}" "《${project.getBookName()}》这本书不错，一起来看看呗"/>"</#if>>分享到微博</a>
                                </li>
                                <li><a class="sweixin" <#if isPreview == false>openurl="#wechatShare"</#if>>分享到微信</a></li>
                                <li><a class="sdouban" target="_blank"
                                       <#if isPreview == false>href="<@zanshang.shareDB "${shareLink}" "《${project.getBookName()}》这本书不错，一起来看看呗"/>"</#if>>分享到豆瓣</a>
                                </li>
                            </ul>
                            <a class="btnShare">&nbsp;</a>
                        </div>

                        <label>
                            分享好友
                        </label>
                    </li>
                <#if bidAuth?exists>
                <li>
                    <a class="btnPublish"
                       <#if isPreview == false>href="${requestContext.contextPath}/projects/${project.getProjectId()}/bid"</#if>></a>
                    <label>
                        出版报价
                    </label>
                </li>
				</#if>
                    <li><a class="btnMore" <#if isPreview == false>href="${requestContext.contextPath}/salons/${project.getUid()}"</#if>></a>
                        <label>
                            沙龙
                        </label>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="zsUserList" id="zsUserList">
    <div class="pc">
        <h3>
            赞赏人</h3>
    <#if isPreview == false>
        <div class="slnBorder">
            <ul class="slnUser">
			<#list allBuyers as buyer>
                <li>
                    <a href="${requestContext.contextPath}/profile/${buyer.getUid()}"><img
                            src="${imageRoot}${buyer.getAvatar()}?imageView2/2/w/72/h/72"/></a>
                    <label>
					${buyer.getDisplayName()}</label>
                </li>
			</#list>
            </ul>
		<#list buyerMap?keys as key>
            <ul class="slnUser">
				<#list buyerMap[key] as buyer>
                    <li>
                        <a href="${requestContext.contextPath}/profile/${buyer.getUid()}"><img
                                src="${imageRoot}${buyer.getAvatar()}?imageView2/2/w/72/h/72"/></a>
                        <label>
						${buyer.getDisplayName()}</label>
                    </li>
				</#list>
            </ul>
		</#list>
        </div>
        <#assign pager=["壹","贰","叁","肆","伍","陆","柒","捌","玖","拾"]>
            <div class="pagers">
                <a class="btn remark">全部
                    <p>共有${allBuyers?size}人赞赏</p>
                </a>
            <#list buyerMap?keys as key>
                <a class="remark">${pager[key_index]}
                    <p>方案${pager[key_index]}共有${buyerMap[key]?size}人赞赏</p>
                </a>
            </#list>
            </div>
        <#if publisher?exists>
            <h3>出版人</h3>
            <ul class="slnUser" id='slnList'>
                <li>
                    <a href="${requestContext.contextPath}/profile/${publisher.id}"><img
                            src="${imageRoot}${publisher.avatar}?imageView2/2/w/72/h/72"/></a>
                    <label>
                    ${publisher.displayName}<#if publisher.organizeName?exists>(${publisher.organizeName})</#if>
                    </label>
                </li>
            </ul>
        </#if>
    </#if>
    </div>
</div>
<#include "./inc/footer.ftl">
<#if isPreview == false>
<div id="fxTools">
    <ul id="leftZs">
	<#list allBuyers as buyer>
		<#if buyer_index gt 10>
			<#break >
		</#if>
        <li><a href="${requestContext.contextPath}/profile/${buyer.getUid()}"><img src="${imageRoot}${buyer.getAvatar()}?imageView2/2/w/72/h/72"/></a></li>
	</#list>
        <li href="#zsUserList">已有${allBuyers?size}人赞赏</li>
    </ul>
    <div class="award">
        <a class="btn" href="${requestContext.contextPath}/projects/${project.getProjectId()}/rewards">去赞赏</a>
    </div>
    <ul class="rightTools">
        <li>
        	<div class="shareMenu">
                <ul class="shadow">
                    <li><a class="sweibo" target="_blank"
                           href="<@zanshang.shareWB "${shareLink}" "《${project.getBookName()}》这本书不错，一起来看看呗"/>">分享到微博</a>
                    </li>
                    <li><a class="sweixin" openurl="#wechatShare">分享到微信</a></li>
                    <li><a class="sdouban" target="_blank"
                           href="<@zanshang.shareDB "${shareLink}" "《${project.getBookName()}》这本书不错，一起来看看呗"/>">分享到豆瓣</a>
                    </li>
                </ul>
                <a class="btnShare">&nbsp;</a>
            </div><a class="btnShare"></a>
        </li>
	<#if bidAuth?exists>
        <li><a class="btnPublish" href="${requestContext.contextPath}/projects/${project.getProjectId()}/bid"></a></li>
	</#if>
        <li><a class="btnMore" href="${requestContext.contextPath}/salons/${project.getUid()}"></a>
        </li>
    </ul>
</div>
<#assign wechatLink=shareQRLink>
<#include "./inc/wechatShare.ftl">
</#if>

    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/jquery.slides.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
</body>
</html>
</#escape>