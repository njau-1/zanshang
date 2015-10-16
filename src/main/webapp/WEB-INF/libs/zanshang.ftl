<#include "/spring.ftl">
<#macro showErrors class="">
	<#list status.errorMessages as error>
    <div class="${class}">${error}</div>
		<#assign errorMessage=error>
		<#break>
	</#list>
</#macro>

<#macro alertErrors>
    <#if errorMessage??>
        <script>alert("${errorMessage}");</script>
    </#if>
</#macro>

<#macro shareWB link content pic="">
http://service.weibo.com/share/share.php?url=${link?url('utf-8')}&type=icon&language=zh_cn&appkey=3832271358&pic=${pic?url('utf-8')}&searchPic=false&style=simple&title=${content?url('utf-8')}
</#macro>

<#macro shareDB link content pic="">
http://www.douban.com/share/service?name=${content?url('utf-8')}&href=${link?url('utf-8')}&image=${pic?url('utf-8')}&updated=&bm=1&url=${link?url('utf-8')}&title=${content?url('utf-8')}&sel=&v=1
</#macro>

<#macro until date curr=.now>
	<#assign diff=(curr?long - date?long)>
	<#if (diff/(1000 * 60 * 60 * 24)) gte 1>
	${(diff/(1000 * 60 * 60 * 24))?floor} 天前
	<#elseif (diff/(1000 * 60 * 60)) gte 1>
	${(diff/(1000 * 60 * 60))?floor} 小时前
	<#elseif (diff/(1000 * 60)) gte 1>
	${(diff/(1000 * 60))?floor} 分钟前
	<#else >
    刚刚
	</#if>
</#macro>
<#macro surround content tag>
${content?replace("\n", "</${tag}><${tag}>")}
</#macro>