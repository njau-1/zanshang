<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>邮件</title>
</head>
<body>
<style type="text/css">
    body {
        background-color: #f5f5f5;
    }

    #emailtab {
        width: 650px;
        margin: 30px auto;
    }

    img {
        border-width: 0;
    }
</style>
<center>
    <table id="emailtab" cellpadding="0" cellspacing="0" width="650" style="margin: 30px auto;">
        <tr>
            <td style="background-color: #fff;  padding:30px; padding-bottom:20px; border-radius: 4px;">
                <table width="100%;">
                    <tr>
                        <td style="border-bottom: 1px solid #eeeeee; text-align: center; padding-bottom:30px;">
                            <img src="${requestContext.contextPath}/static/img/logo.png" width="80"/>
                            <!--发邮件的时候，请把页面所有图片地址换成http://带域名的绝对地址，此页样式请保留成内联样式-->
                        </td>
                    </tr>
                    <tr>
                        <td style=" font-size:18px; color:#000; padding-top:30px; padding-bottom:10px;">
                            <strong>尊敬的用户，您好：</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style="color:#5c5c5c; font-size:16px; line-height:26px;">
                            您的项目
                            <a href="http://www.zan-shang.com/projects/${projectId}">《${bookName}》</a>还有一周筹款结束，请再接再厉继续推广!
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;color:#191c1c; font-size:16px; line-height:24px; padding-bottom:20px;">
                            此致，<br/>
                            赞赏运营小组敬上
                        </td>
                    </tr>
                    <tr>
                        <td style="padding-top: 20px; border-top: 1px solid #eeeeee;">
                            <table cellpadding="0" width="100%">
                                <tr>
                                    <td width="33%" style="text-align: center; width: 33%;">
                                        <img src="${requestContext.contextPath}/static/img/zsserver.png" width="80"/>

                                        <p>
                                            微信服务号</p>
                                    </td>
                                    <td width="33%" style="text-align: center; width: 33%;">
                                        <img src="${requestContext.contextPath}/static/img/zsdingyue.png" width="80"/>

                                        <p>
                                            微信订阅号</p>
                                    </td>
                                    <td width="33%" style="text-align: center; width: 33%;">
                                        <a href="mailto:service@zan-shang.com">
                                            <img src="${requestContext.contextPath}/static/img/email.png" height="75" alt="邮箱：service@zan-shang.com"/></a>

                                        <p>
                                            <a href="mailto:service@zan-shang.com">服务邮箱</a>
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</center>
</body>
</html>