<#escape x as x?html><#import "/libs/zanshang.ftl" as zanshang/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${project.getAuthorName()}-${project.getBookName()}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="shareTip">
    <label> 打开右上角微信分享</label>
</div>
<div style="display: none;">
    <img src="${imageRoot}${project.getCover()}?imageView2/1/w/400/h/400"/>
</div>
<div id="bookCover">
            <img src="${imageRoot}${project.getCover()}?imageMogr2/blur/10x5&imageView2/2/w/304/h/450" class="blur" />
            <div id="topBookMsg">
                <img src="${imageRoot}${project.getCover()}?imageView2/2/w/148"/>
                <div class="bookName">
                    ${project.getBookName()}</div>
                <div class="bookAuthor">
                    ${project.getAuthorName()}</div>
                <ul class="bookState">
                    <li <#if project.getCurrentMoney() gte project.getGoal()>class="suc"</#if>>
                        <div class="progress">
							${(project.getCurrentMoney()/project.getGoal() * 100)?floor}%</div>
                        <div>
                            已筹</div>
                    </li>
                    <li <#if project.isDeadline()>class="suc"</#if>>
                        <div class="progress">
							${project.getPassedDays()} / ${project.getTotalDays()}
						</div>
                        <div>
                            进行天数</div>
                    </li>
                    <li <#if project.isPublisherLocked()>class="suc"</#if>>
                        <div class="progress">
                            /</div>
                        <div>
                            出版支持</div>
                    </li>
                </ul>
            </div>
        </div>
        <p class="bookDetails pre">${project.getDescription()?html}</p>
        <div class="btnRight">
            <div id="leftTools">
                <a class="btn btnShare" id="btnshare">&nbsp;</a>
                <a class="btn btnMore" href="${requestContext.contextPath}/salons/${project.getUid()}">&nbsp;</a>
            </div>
            <a class="btn" href="${requestContext.contextPath}/projects/${project.getProjectId()}/mobile">查看图书详情</a>
        </div>
    <#assign number=["壹","贰","叁","肆","伍","陆","柒","捌","玖","拾"]>
		<#list rewards as reward>
            <div class="sln">
                <ul>
                    <li>
                        <h3>
                            ${number[reward_index]}
                        </h3>
                        
                        <div class="zsList">
                            <#list buyerMap[reward.getId()] as buyer>
                            	<#if buyer_index gt 1>
                            		<#break >
                            	</#if>
	                                <img src="${imageRoot}${buyer.getAvatar()}?imageView2/2/w/32/h/32" />
                            </#list>
                            <a class="more" href="${requestContext.contextPath}/projects/${project.getProjectId()}/mobile/buyer?index=${reward_index}"></a>
                            <label>
                                <a href="${requestContext.contextPath}/projects/${project.getProjectId()}/mobile/buyer?index=${reward_index}">
                         		    ${buyerMap[reward.getId()]?size}人已支持
                                </a>
                         	</label>
                        </div>
                        
                    </li>
                    <li class="slntt"><h5>将获赠</h5>
                    </li>
                    <li class="gift">
					<#assign keys=reward.items?keys>
					<#list keys as key>
	                    <#if key == "BOOK">
	                        <a class="btn">${reward.items[key].getCount()}本《${project.getBookName()}》</a>
	                    </#if>
	                    <#if key == "VIP">
	                        <a class="btn">${reward.items[key].getCount()}个沙龙VIP</a>
	                    </#if>
	                    <#if key == "SIGNATURE">
	                        <a class="btn">${reward.items[key].getCount()}个作者签名</a>
	                    </#if>
                        <#if key == "OTHER">
	                        <a class="btn">${reward.items[key].getDetail()}</a>
	                    </#if>
					</#list>
                    </li>
                    <li class="btnRight">
                        <#if project.getProjectId() == "55e65547e4b033b99cf284dd" && reward_index == 2>
                            <a class="btn"
                               onclick="alert('此方案已达限购名额，请赞赏其他方案！')">赞赏${reward.getPrice().to("YUAN")}
                                元</a>
                        <#else>
                            <a class="btn award" href="${requestContext.contextPath}/orders/creation?reward=${reward.getId()}">赞赏${reward.getPrice().to("YUAN")?c}元</a>
                        </#if>
                    </li>

                </ul>
            </div>

		</#list>
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
        <#assign menu_projects=true>
        <#include "../inc/menu.ftl">
</body>
</html>
</#escape>