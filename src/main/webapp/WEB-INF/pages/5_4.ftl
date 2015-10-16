<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>回报和筹款设置</title>
	<#include "./inc/head.ftl">
</head>
<body>
<#include "./inc/header.ftl">
<form action="${requestContext.contextPath}/projects/${project.getId()}/pricing" method="post">
    <div id="pc">
        <div id="center">
            <h3 class="tt">
                《${project.getBookName()}》回报筹款设置</h3>
            <ul class="topBookMsg">
                <li><a class="remark">赞赏截止时间
                    <p>
                        截止时间越短越容易吸引用户赞赏，建议勿超过90天；截止时间前，您必须交付全部书稿。</p>
                </a>
                    <input class="date" type="text" name="deadline"
                           placeholder="点击选择截止时间" onclick="WdatePicker({skin:'whyGreen',dateFmt:'dd/MM/yyyy',minDate:'%y-%M-{%d+1}',maxDate:'%y-%M-{%d+90}'})"/>
                </li>
                <li><span>选择赞赏版本</span>
                    <label class="remark">
                        <input type="radio" name="rd_ver" value="1" checked="checked" />数字订制版
                        <p>
                            选取较市场版更好的纸墨动态精印，每册可分别编号并印出不同赞赏人姓名，作者需付出更高成本
                        </p>
                    </label>
                    <label class="remark">
                        <input type="radio" name="rd_ver" value="0" />普通订制版
                        <p>
                            普通赞赏版即市场版加装赞赏签名页，作者可免费获取一定量图书，如需更多图书可以折扣定价购买
                        </p>
                    </label>
                </li>
            </ul>
            <div id="calculate">
                <div class="arrowUp" id="arrowBorder">
                    &nbsp;</div>
                <div class="arrowUp">
                    &nbsp;</div>
                <ul>
                    <li id="setPrice">
                        <input type="text" placeholder="输入方案壹的回报价格" class="txt number" /><a class="btn">计算</a></li>
                    <li id="minbookCount">最低赞赏本数：<label></label>本 </li>
                    <li id="minPrice">最低筹款金额：<label></label>元 </li>
                </ul>
            </div>
            <ul id="priceSm">
                <li>数字订制版有更优品质，更个性生产，相应成本也高于普通版</li>
                <li>筹款金额已含出版印刷和邮寄成本</li>
            </ul>
            <div class="bookSolu">
                <div class="tt">
                    壹
                </div>
                <ul class="slnList">
                    <li>
                        <img src="${imageRoot}${project.getCover()}" class="shadow"/>

                        <div class="bookCount">
                            实体书×<label>1</label></div>
                    </li>
                    <li class="add">+</li>
                    <li>
                        <img src="${requestContext.contextPath}/static/img/vip.png"/>

                        <div class="bookCount">
                            沙龙会员×<label>1</label></div>
                    </li>
                    <li class="add">+</li>
                    <li>
                        <img src="${requestContext.contextPath}/static/img/sign.png"/>

                        <div class="bookCount">
                            作者签名×<label>1</label></div>
                    </li>
                </ul>
                <ul class="bookReg">
                    <li>
                        <label id="lab_p1">
                        </label>
                        <label>
                            /元</label>
                    </li>
                </ul>
            </div>
            <div class="bookSolu dn">
                <div class="btnClose" title="删除这个方案">
                    &nbsp;</div>
                <div class="tt">
                    贰
                </div>
                <ul class="slnList">
                    <li>
                        <img src="${imageRoot}${project.getCover()}" class="shadow"/>

                        <div class="bookCount">
                            <!-- 设置min指定允许输入的最小值，设置max指定允许输入的最大值-->
                            实体书×
                            <div class="ckNum" min="1">
                                <label>
                                    1</label><a class="arrowUp"></a><a class="arrowDown"></a></div>
                        </div>
                    </li>
                    <li class="add">+</li>
                    <li>
                        <img src="${requestContext.contextPath}/static/img/vip.png"/>

                        <div class="bookCount">
                            沙龙会员×<label>1</label></div>
                    </li>
                    <li class="add">+</li>
                    <li>
                        <img src="${requestContext.contextPath}/static/img/sign.png"/>

                        <div class="bookCount">
                            作者签名×<label>1</label></div>
                    </li>
                </ul>
                <div class="addOther">
                    <table>
                        <tr>
                            <td style="vertical-align: top;">
                                <label>
                                    +</label>
                            </td>
                            <td>
                                <textarea placeholder="其它礼物"></textarea>
                            </td>
                        </tr>
                    </table>
                </div>
                <ul class="bookReg">
                    <li>
                        <input type="text" class="txt txtcost number" placeholder="输入礼物成本（系统用于计算回报最低价）"/><label>/元</label>
                    </li>
                    <li>
                        <input type="text" class="txt txtprice number" placeholder="输入回报价格"/><label>/元</label>
                    </li>
                </ul>
            </div>
            <div class="inputMoney">
                <input type="text" class="txt number" id="goal" name="goal" placeholder="最终筹款金额"/>
            </div>
            <div class="btnList btnMg">
                <a class="btn" id="btnSln">添加回报方案</a><a class="btn" class="btn" value="发布" id="btnSlnData">发布</a>
            </div>
            <input type="submit" class="dn" id="btnSave" />
            <div id="hd_list">
            </div>
        </div>
    </div>
    <!--  后台反馈最低出版资金C-->
    <input type="hidden" id="hd_c" value="${feedbackGoal?c}" />
    <!--  字数-->
    <input type="hidden" id="hd_words" value="${project.getWordCount()?c}" />
    <!--  图片张数-->
    <input type="hidden" id="hd_pic" value="${project.getImageCount()?c}" />
</form>
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
</body>
</html>
</#escape>