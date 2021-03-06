﻿<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>添加新收件信息</title>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <link href="${requestContext.contextPath}/static/mobile/css/base.css" rel="stylesheet" type="text/css" />
    <script>var server= "${requestContext.contextPath}/";</script>
</head>
<body>
  <form action="" method="post">
<div class="layer">
        <div class="layerC">
            <div class="layerBody">
                <div class="ttFont">
                    添加新收件信息
                </div>
                  <div class="reg">
                    <ul>
                        <li>
                            <input type="text" placeholder="姓名" class="txt test required" id="txtName" />
                        </li>
                        <li>
                            <input type="text" placeholder="手机号码" class="txt test required mobile" id="txtMob" />
                        </li>
                        <li class="area">
                            <select id="sls_pro" name="sls_pro">
                                <option value="">省份</option>
                            </select>
                            <select id="sls_city" name="sls_city">
                                <option value="">城市</option>
                            </select>
                            <select id="sls_dist" name="sls_dist">
                                <option value="">区县</option>
                            </select>
                        </li>
                        <li>
                            <input type="text" placeholder="街道地址" maxlength="100" class="txt test required" id="txtAddr" />
                        </li>
                         <li>
                            <input type="text" placeholder="邮编" class="txt zipCode test required" id="txtPost" />
                        </li>
                    </ul>
                </div>
                <input type="hidden" name="return" id="return" value="<#if return??>${return}</#if>"/>
                <div class="btnList">
                    <a class="btn btnDfAddr" id="btnSaveDefAddr">确认同时设为默认地址</a><a class="btn" id="btnSaveAddr">确定</a>
                </div>
            </div>
        </div>
    </div>
    </form>
  <script src="${requestContext.contextPath}/static/mobile/js/jquery-1.4.1.min.js" type="text/javascript"></script>
  <script src="${requestContext.contextPath}/static/mobile/js/test.js" type="text/javascript"></script>
  <script src="${requestContext.contextPath}/static/mobile/js/base.js" type="text/javascript"></script>
  <script src="${requestContext.contextPath}/static/js/city.js" type="text/javascript"></script>
</body>
</html>
</#escape>