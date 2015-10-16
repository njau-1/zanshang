<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>注册成功</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <style>
        #register-success{
            position:fixed;
            top:0;
            left:0;
            width:100%;
            height:100%;
            display:table;
        }
        .center{
            display: table-cell;
            vertical-align: middle;
            text-align:center;
            font-size:20px;
        }
        .btn{
            display: block;
            line-height: 40px;
            width: 100px;
            margin: 0 auto;
            border-radius: 30px;
            margin-top: 30px;
            font-size: 18px;
            text-decoration: none;
            color: rgba(247, 129, 145, 0.9);
            cursor: pointer;
            border: 1px solid rgba(217, 217, 217, 0.6);
        }
    </style>
</head>
<body>
<div id="register-success">
    <div class="center">
        赞赏注册成功！</br>
        <a href="${requestContext.contextPath}/authentication<#if return??>?return=${return}<#else>?return=/</#if>" class="btn">登录</a>
    </div>
</div>
</body>
</html>