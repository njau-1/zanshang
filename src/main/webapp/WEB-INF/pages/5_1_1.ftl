<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>发起项目</title>
	<#include "./inc/head.ftl">
    <script>var server = "${requestContext.contextPath}/";</script>
</head>
<body>
<#assign border=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div id="leftMenu">
        <div class="curTag">
            &nbsp;</div>
        <ul>
            <li class="cur"><a href="">图书信息</a></li>
            <li><a href="">作者信息</a></li>
            <li><a href="">估算单本成本</a></li>
            <li><a href="">反馈最低出版资金</a></li>
            <li><a href="">回报和筹款设置</a></li>
            <li><a href="">发布项目</a></li>
        </ul>
    </div>
    <div id="editContent">
        <div id="page1" class="page defaultShow">
            <div class="topMsg">
                <input type="text" class="txtBookName" id="txtBookName" placeholder="书名" maxlength="50"
                       value="<#if name??>${name}</#if>"/>

                <div class="bookPic btnUpload">
				<#if cover??><img src="${imageRoot}${cover}"><#else><img
                        src="${imageRoot}/static/default_cover.png"></#if>
                    <span>封面</span>
                </div>
            </div>
            <!-- <div class="category menu dn">
                <a>
                    <label>
					<#if type??>${type}<#else >类别</#if></label><span class="arrowDown">&nbsp;</span>
                    <ul>
                        <li>文学</li>
                        <li>流行</li>
                        <li>文化</li>
                        <li>生活</li>
                        <li>经管</li>
                        <li>科技</li>
                    </ul>
                </a>
            </div>-->
            <div id="categorys">
                <div id="ckList">
                    <div id="ck">
                        <label>
                            类别</label><span class="arrowDown">&nbsp;</span></div>
                    <ul id="ckTag">
                        <!--编辑的时候已选标签就写在这里-->
                        <#if types?? && types?size gt 0>
                            <#list types as type>
                                <li>${type}</li>
                            </#list>
                        </#if>
                    </ul>
                </div>
                <div id="hovercMenu">
                    <div id="cmenu" class="shadow">
                        <ul>
                            <li>创业</li>
                            <li>创新</li>
                            <li>创意</li>
                            <li>互联网</li>
                            <li>科技</li>
                            <li>情感</li>
                            <li>女性</li>
                            <li>心理</li>
                            <li>儿童</li>
                            <li>健康</li>
                            <li>旅行</li>
                            <li>城市</li>
                            <li>音乐</li>
                            <li>时尚</li>
                            <li>美食</li>
                            <li>名人</li>
                            <li>传记</li>
                            <li>小说</li>
                            <li>诗歌</li>
                            <li>散文</li>
                            <li>历史</li>
                            <li>思想</li>
                            <li>教育</li>
                            <li>财经</li>
                            <li>投资</li>
                        </ul>
                    </div>
                </div>
            </div>
            <textarea id="area_description" class="text" style="max-height:300px;"
                      placeholder="关于书"><#if description??>${description}</#if></textarea>

            <div class="tagList remark">
                <div class="tagControl">
				<#if tags?? && tags?size gt 0>
					<#list tags as tag>
                        <span>${tag}</span>
					</#list>
				</#if>
                </div>
                <input type="text" class="txt" placeholder="添加标签" maxlength="10"/>

                <p>
                    输入空格自动区分标签</p>
            </div>
            <div class="uploadImgList">
                <div class="img1 btnUpload">
				<#if images?? && images[0]??>
                    <img src="${imageRoot}${images[0]}">
				<#else >
                    <span>相关配图</span>
				</#if>
                </div>
                <div class="img2 btnUpload">
				<#if images?? && images[1]??>
                    <img src="${imageRoot}${images[1]}">
				<#else >
                    <span>相关配图</span>
				</#if>
                </div>
                <div class="img3 btnUpload">
				<#if images?? && images[2]??>
                    <img src="${imageRoot}${images[2]}">
				<#else >
                    <span>相关配图</span>
				</#if>
                </div>
            </div>
            <textarea id="area_outline" placeholder="大纲" class="text" style="max-height:300px;"><#if outline??>${outline}</#if></textarea>
            <textarea id="area_draft" placeholder="样稿(项目发布后将显示到沙龙中)" class="text" style="max-height:300px;"><#if draft??>${draft}</#if></textarea>

            <div class="btnList">
                <a class="btn btnNext" id="btn_add1">下一步</a>
            </div>
        </div>
        <div id="page2" class="page">
            <div class="tagList">
                <div class="tagControl">
                    <span isdel="false">${setting.getDisplayName()}</span>
                </div>
            </div>
            <textarea class="text" placeholder="关于我" style="max-height:300px;"
                      id="lab_aboutFirstAuthor"><#if aboutFirstAuthor??>${aboutFirstAuthor}</#if></textarea>
            <div class="authorMsg">
            <div class="dn">
            	<ul>
                    <li>
                        <div class="tagList remark">
                            <input type="text" class="txt" placeholder="其他作者姓名" maxtagcount="1" />
                            <p>添加的作者必须注册赞赏并补全身份信息，项目才能通过审核</p>
                        </div>
                    </li>
                    <li>
                        <div class="tagList">
                            <input type="text" class="txt" placeholder="其他作者身份证号码 (添加后不可修改)" maxtagcount="1" />
                        </div>
                    </li>
                    <li>
                        <textarea class="text" placeholder="关于其他作者的介绍…" style="max-height:300px;"></textarea>
                    </li>
                    <li>
                        <div class="line">
                            &nbsp;</div>
                        <a class="btn btnDel">删除</a> </li>
                </ul>
            </div>
			<#if authors??>
				<#list authors as author>
                    <ul>
                        <li>
                            <div class="tagList remark">
                                <div class="tagControl">
                                    <span del="0" title="" class="">${author.getName()}</span>
                                </div>
                                <input type="text" class="txt" placeholder="其他作者名称" maxtagcount="1" value="${author.getName()}" autocomplete="off" style="display: none;">
                                <p>添加的作者必须注册赞赏并补全身份信息，项目才能通过审核</p>
                            </div>
                        </li>
                        <li>
                            <div class="tagList">
                                <div class="tagControl">
                                    <span del="0" title="" class="">${author.getIdentity()}</span>
                                </div>
                                <input type="text" class="txt" placeholder="其他作者身份证号码" maxtagcount="1" value="${author.getIdentity()}" autocomplete="off" style="display: none;">
                            </div>
                        </li>
                        <li>
                        	<textarea class="text" placeholder="关于作者…" id="lab_aboutFirstAuthor" style="max-height:300px;">${author.getDescription()}</textarea>
                        </li>
                        <li>
                            <div class="line">
                                &nbsp;</div>
                            <a class="btn btnDel">删除</a></li>
                    </ul>
				</#list>
			</#if>
            <a class="btn btnAuthor">添加作者</a>
            </div>
            <div class="btnList btnMg">
                <a class="btn btnPrev">上一步</a> <a class="btn btnNext">下一步</a>
            </div>
        </div>
        <div id="page3" class="page">
            <div class="smallPageC">
                <ul class="noBorderReg">
                    <li>
                        <input type="text" placeholder="字数" class="txt number" id="txtwordCount"
						       <#if wordCount?? && wordCount gt 0>value="${wordCount?c}"</#if>/></li>
                    <li>
                        <input type="text" placeholder="图片张数" class="txt number" id="txtimageCount"
						       <#if imageCount?? && imageCount gt 0>value="${imageCount?c}"</#if>/></li>
                    <li>
                        <label>
                            <input type="radio" name="rd_print" value="0"
                                   <#if color?? && color == 0>checked="checked"</#if>/>整书不彩印</label>
                        <label>
                            <input type="radio" name="rd_print" value="1"
							       <#if color?? && color == 1>checked="checked"</#if>/>仅图片集中彩印</label>
                        <label>
                            <input type="radio" name="rd_print" value="2"
							       <#if color?? && color == 2>checked="checked"</#if>/>整书彩印</label>
                    </li>
                </ul>
                <div class="btnList btnMg">
                    <a class="btn btnPrev">上一步</a>
                    <!-- <a class="btn" id="tempSave">暂存</a> -->
                    <a class="btn" target="_blank" id="tempSave">预览</a>
                    <!--  如果是编辑请设置一下projectid,否则为空就是新增-->
                    <a class="btn" id="btnLastSave" projectid="<#if projectId??>${projectId}</#if>"
                       isopen="${isOpen?c}">提交</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="win dn" id="page5_6">
    <div class="layer">
        <div class="btnClose">&nbsp;</div>
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                    作者补全信息
                </div>
                <div class="bz">
                    还差一步项目就创建成功了，请补全下列信息
                </div>
                <div class="reg">
                    <ul>
                        <li>
                            <input type="text" placeholder="真实姓名" class="txt" id="txtName"
							       <#if legalname??>value="${legalname}"</#if>/>
                        </li>
                        <li>
                            <input type="text" placeholder="身份证号码" class="txt" id="txtId"
							       <#if authorId??>value="${authorId}"</#if>/>
                        </li>
                    </ul>
                </div>
                <div class="picList">
                    <div class="pic btnUpload" id="imgId">
					<#if authorFront??><img src="${imageRoot}${authorFront}"></#if>
                        <span>身份证正面</span></div>
                    <div class="line">
                        &
                    </div>
                    <div class="pic btnUpload" id="imgIdBack">
					<#if authorBack??><img src="${imageRoot}${authorBack}"></#if>
                        <span>身份证背面</span></div>
                </div>
                <div class="btnList">
                    <a class="btn" id="btnAddAuthor">确定</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="win dn" id="page5_7">
    <div class="layer">
        <img src="${requestContext.contextPath}/static/img/logo.png" id="logo"/>

        <div class="layerC">
            <div class="itemSuc">
                <div class="ttFont">项目已提交审核！</div>
                <p class="sucTip">审核期间您可以到 <a href="${requestContext.contextPath}/profile/projects">参与的项目</a> 中对项目继续修改完善。审核反馈最低出版资金后您可以设置回报方案和筹款金额。
                    请加入赞赏培训营QQ群（群号：368267540）获取更多帮助信息。</p>

                <div class="btnList">
                    <label><span>10</span>秒后转到</label><a class="btn"
                                                                   href="${requestContext.contextPath}/profile/projects">参与的项目</a>
                </div>
            </div>
        </div>
    </div>
</div>
    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>