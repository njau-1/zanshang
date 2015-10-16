<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>出版商报价</title>
	<#include "./inc/head.ftl">
</head>
<script>var server = "${requestContext.contextPath}/";</script>
<body>
<#assign border=true>
<#include "./inc/header.ftl">
<div class="layer">
    <div class="layerC">
        <div class="publisherList">
            <div class="ttFont">
                出版商报价
            </div>
            <div class="price">
                <#if bids?size == 0>
                    <div class="noData">暂无报价！</div>
                    <table cellpadding="0" cellspacing="0" class="table">
                    </table>
                <#else>
                    <table cellpadding="0" cellspacing="0" class="table">
                    <#list bids as bid>
                        <#if publisherId?? && publisherId == bid.uid>
                            <tr class="cheap">
                                <td>
                                ${bid.displayName}<#if bid.organizeName?exists>(${bid.organizeName})</#if>
                                </td>
                                <td>
                                ${bid.price} /元
                                </td>
                                <td class="result">
                                    胜出
                                </td>
                            </tr>
                        <#else>
                            <tr>
                                <td>
                                ${bid.displayName}<#if bid.organizeName?exists>(${bid.organizeName})</#if>
                                </td>
                                <td>
                                ${bid.price} /元
                                </td>
                                <td class="result">
                                    &nbsp;
                                </td>
                            </tr>
                        </#if>
                    </#list>
                </#if>
                </table>
            </div>
		<#if bidAuth?exists && !publisherId??>
            <div class="inputPrice">
                <input type="text" class="txt number" placeholder="输入报价" />
                <a class="btn" projectid="${projectId}">确定</a>
            </div>
		</#if>
        </div>
    </div>
</div>
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/ajax.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>