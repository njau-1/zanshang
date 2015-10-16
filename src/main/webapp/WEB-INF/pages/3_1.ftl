<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/html">
<head>
    <title>项目浏览</title>
    <meta name="description"
          content="全站最值得注意的项目列表"/>
	<#include "./inc/head.ftl">
</head>
<body>
<#assign border=true>
<#assign header_projects=true>
<#include "./inc/header.ftl">
<div id="pc">
    <div class="authorList" id="projects">
	<#assign list=projects.getContent()>
	<#list list as project>
        <div class="author">
            <div class="left">
                <div class="zsAuthor">
                    <a href="${requestContext.contextPath}/profile/${project.getUid()}">
                        <img src="${project.getAvatar()}">
                        <label>
						${project.getAuthorName()}</label>
                    </a>
                </div>
                <ul class="bookState">
                    <li <#if project.getCurrentMoney() gte project.getGoal()>class="suc"</#if>>
                        <div class="progress">
						${(project.getCurrentMoney()/project.getGoal() * 100)?floor}%
                        </div>
                        <div>
                            已筹
                        </div>
                    </li>
					<#assign aDateTime = .now>
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
                </ul>
            </div>
            <div class="center">
                <h3 class="bookName"><a href="${requestContext.contextPath}/projects/${project.getProjectId()}">
				${project.getBookName()}</a></h3>
                <ul class="bookTagList">
                    <#if project.getTypes()??>
                        <#list project.getTypes() as type>
                            <li><a class="tag btn">${type}</a></li>
                        </#list>
                    </#if>
                </ul>
                <p class="pre">${project.getDescription()}</p>
            </div>
            <div class="rightBook">
                <a href="${requestContext.contextPath}/projects/${project.getProjectId()}">
                    <img src="${project.getCover()}?imageView2/2/w/278/h/376"/>
                </a>
            </div>
        </div>
	</#list>
        <div class="more" page="0" size="10">
            载入更多
        </div>
    </div>
</div>
<#include "./inc/footer.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
</body>
</html>
</#escape>