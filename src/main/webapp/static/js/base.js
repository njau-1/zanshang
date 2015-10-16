var calculate = {
    isFirst: function () {
        return $("input[name='rd_ver']").index($("input[name='rd_ver']:checked")[0]) == 0;
    },
    min: function () {
        var A = 100;
        if (calculate.isFirst()) {
            A = 15 + 10 + 0.2 * (parseFloat($("#hd_words").val()) / 550 + parseFloat($("#hd_pic").val()));
            if ((A + 50) < 100) {
                A = 100;
            }
        }
        A = Math.round(A);
        $("#setPrice .txt").attr("min", A);
        return A;
    },
    minNumber: function () {
        var A = 100;
        if (calculate.isFirst()) {
            A = 15 + 10 + 0.2 * (parseFloat($("#hd_words").val()) / 550 + parseFloat($("#hd_pic").val()));
            if ((A + 50) < 100) {
                A = 100;
            } else {
                A = A + 50;
            }
        }
        A = Math.round(A);
        return A;
    },
    minPrice: function (obj) {
        if ($.trim(obj.val()) == "") {
            return false;
        }
        var pobj = obj.parents(".bookSolu");
        var bookCount = parseInt(pobj.find(".ckNum label").text());
        var constN = 1.1;
        if (pobj.find(".tt").text() == "叁") {
            constN = 1.2;
        }
        else if (pobj.find(".tt").text() == "肆") {
            constN = 1.3;
        }
        var s2 = constN * bookCount * parseFloat($("#setPrice .txt").val()) + parseFloat(obj.val());
        s2 = Math.round(s2);

        pobj.find(".txtprice").attr({ "min": s2, "placeholder": "输入回报价格（建议价格不低于" + s2 + "）" });
    },
    init: function () {
        calculate.min();
        $("#setPrice .txt").blur(function () {
            $("#lab_p1").text($(this).val());
        });
        $(document).on("blur", "input.txtcost", function () {
            calculate.minPrice($(this));
        });
        $("#setPrice .btn").click(function () {
            var A = calculate.min();
            var p1 = $("#setPrice .txt");
            if ($.trim(p1.val()) == "") {
                tip("请先输入方案壹回报价格", p1);
                p1.focus();
                return false;
            }
            var minNumber = calculate.minNumber();
            if (parseFloat(p1.val()) < minNumber) {
                tip("所填的方案壹回报价格不能小于" + minNumber, p1);
                p1.focus();
                return false;
            }
            var C = parseFloat($("#hd_c").val());
            var N = 0, M = 0;
            var pval1 = parseFloat(p1.val());
            if (calculate.isFirst()) {
                //精印赞赏版
                N = Math.round(C / ((0.9 * (pval1 - 8)) - A));
                M = Math.round((C + A * N) / 0.9 + 8 * N);
            }
            else {
                N = Math.round(C / (0.9 * (pval1 - 8)));
                M = Math.round(C / 0.9 + 8 * N);
            }
            $("#minbookCount label").text(N);
            $("#minPrice label").text(M);
        });
    }
};
var tab = {};
function alert(error) {
    $("#tip").remove();
    if (!error) {
        error = "所填不能为空！";
    }
    var styleStr = 'style="margin-left:-' + (error.length * 16) / 2 + 'px"';
    var tipObj = $('<div id="tip"  ' + styleStr + '>' + error + '<a class="close">&nbsp;</a></div>');
    tipObj.find("a.close").click(function () {
        $("#tip").remove();
    });
    $("body").append(tipObj);
    $("#tip").fadeIn("normal");
    var ct2 = setTimeout(function () {
        clearTimeout(ct2);
        $("#tip").remove();
    }, 2000);
}
function tip(error, obj) {
    $("#tip").remove();
    if (!error) {
        error = "所填不能为空！";
    }
    var hg = obj.offset().top;
    var wd = obj.offset().left;
    if (!hg) {
        hg = 50;
    }
    else {
        hg = parseFloat(hg) - 80;
    }
    var styleStr = '';
    if ((obj[0].tagName == "INPUT" && obj.attr("type") != "button") || (obj[0].tagName == "TEXTAREA")) {
        styleStr = 'style="top:' + hg + 'px;"';
        obj.focus();
    }
    else {
        styleStr = 'style="top:' + hg + 'px;left:' + wd + 'px"';
    }
    var tipObj = $('<div id="msgTip"  ' + styleStr + '>' + error + '</div>');

    $("body").append(tipObj);
    $("#msgTip").css("margin-left", 1 - parseFloat($("#msgTip").width()) / 2);
    $("#msgTip").fadeIn("normal");
    var ct = setTimeout(function () {
        clearTimeout(ct);
        $("#msgTip").remove();
    }, 2000);
}
function isEmail(szMail) {
    var szReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var bChk = szReg.test($.trim(szMail));
    return bChk;
}
function isMob(mob) {
    return $.trim(mob).length === 11;
}

tab.show = function () {
    /// <summary>切换选项卡</summary>
    $("ul.tab li").click(function () {
        $(".tabHide").css("display", "none");
        var idx = $(this).index();
        if ($(".tabHide").size() < $("ul.tab li").size()) {
            idx--;
            $(".tabHide,.tabHide ul").css("border-top", "1px solid #d9d9d9");
        }
        if (idx > -1) {
            $(".tabHide").eq(idx).css("display", "block");
        }
        $("ul.tab li.cur").removeClass("cur");
        $(this).addClass("cur");

    });
    //    $("ul.tab2 li").click(function () {
    //        $(".tabHide").css("display", "none");
    //        var idx = $(this).index();
    //        if (idx > -1) {
    //            $(".tabHide").eq(idx).css("display", "block");
    //        }
    //        $("ul.tab2 li").removeClass("cur");
    //        $(this).addClass("cur");
    //        ui.pf();
    //    });
};
tab.leftMenu = function () {
    /// <summary>调整左侧菜单左边的横杆的位置</summary>
    if (document.getElementById("leftMenu")) {
        $("#leftMenu .curTag").css("top", parseFloat($("#leftMenu li.cur").position().top) - 10);
    }
};
tab.init = function () {
    tab.show();
    tab.leftMenu();
};
var layer2 = {};
layer2.close = function () {
    /// <summary>关闭弹层</summary>
    $(".layer .btnClose").click(function () {
        $(this).parents(".win").fadeOut("fast", function () {
            parent.layer2.closeMy();
        });
        $(".win").fadeOut("fast");
        $("body").css("overflow-y", "auto");
    });
};
layer2.closeMy = function () {
    $(".win").css("display", "none");
};
layer2.open = function (urlOrHtml) {
    /// <summary>显示弹层</summary>
    /// <param name="urlOrHtml" type="String">弹层页面地址或html标签</param>

    if (urlOrHtml.indexOf("#") != -1) {
        $(urlOrHtml).fadeIn("normal");

    }
    else {
        $(".win").remove();
        var ht = '<div class="win">';
        ht += '<iframe src="' + urlOrHtml + '" width="100%" height="100%">';
        ht += '</div>';
        $("body").append(ht);
        $(".win").fadeIn("normal");
    }
    $("body").css("overflow", "hidden");

};
layer2.init = function () {
    $(document).on("click", "a[openUrl]", function () {
        if ($(this).attr("openUrl") === "#page7_11") {
            editAddrObj = $(this).parents("td");
        }
        $(".win").css("display", "none");
        $("body").css("overflow-y", "auto");
        layer2.open($(this).attr("openUrl"));
        return false;
    });
    layer2.close();
};
var reg = {};

reg.tip = function (idOrObj, msg) {
    /// <summary>在表单右侧弹出提示</summary>
    /// <param name="idOrObj" type="Object">参数为表单的id或者dom对象</param>
    /// <param name="msg" type="String">要提示的文字内容</param>
};
reg.edit = function () {
    /// <summary>单击编辑按钮后的显示效果</summary>
    $("a.btnEdit").click(function () {
        var $obj = $(this);
        $obj.animate({ "margin-right": "-200px" }, "fast", function () {
            $obj.css("display", "none").parents("li").find(".btnHide").animate({ "margin-right": 0 }, "normal")
                .end().find(".txt").attr("disabled", false).focus();
        });
        return false;
    });
    $("a.btnCancel").click(function () {
        var $obj = $(this).parents("li");
        $obj.find(".btnHide").animate({ "margin-right": "-200px" }, "fast", function () {
            $obj.find(".txt").attr("disabled", true);
            $obj.find(".btnEdit").css("display", "block").animate({ "margin-right": "0" }, "normal");
        });

        return false;
    });
};
reg.oldColor = "";
reg.focus = function () {
    /// <summary>表单获得焦点后下划线变色</summary>
    $(".bigReg .txt").focus(function () {
        reg.oldColor = $(this).parents("li").css("border-bottom-color");
        $(this).parents("li").css("border-bottom-color", "#71bc09");
    }).blur(function () {
        $(this).parents("li").css("border-bottom-color", reg.oldColor);
    });
};
reg.oldHg = 0;
reg.setxtHeight = function () {
    /// <summary>敲回车增加textarea的高度</summary>
    $("textarea").keyup(function () {
        if (reg.oldHg !== this.scrollHeight) {
            if ($(this).attr("max-height") && parseFloat($(this).attr("max-height")) <= this.scrollHeight) {
                $(this).height(parseFloat($(this).attr("max-height")));
                reg.oldHg = parseFloat($(this).attr("max-height"));
            }
            else {
                $(this).height(this.scrollHeight);
                reg.oldHg = this.scrollHeight;
            }
        }
    });
};
reg.price = 0;
reg.addNumber = function () {
    /// <summary>自定义的加数字的控件</summary>
    $(".ckNum a.arrowUp").click(function () {
        var $obj = $(this).parents(".ckNum");
        var max = $obj.attr("max");
        var curCount = parseInt($obj.find("label").text());
        if (curCount == parseInt(max)) {
            return true;
        }
        $obj.find("label").text(curCount + 1);
        if (document.getElementById("btnOrder")) {
            //6_4价格跟着数量变化
            var orderPrice = $("#btnOrder").find("label");
            if (reg.price == 0) {
                reg.price = parseFloat(orderPrice.text());
            }
            orderPrice.text(reg.price * (curCount + 1));
        }
        $("#count").val(curCount + 1);
        calculate.minPrice($(this).parents(".bookSolu").find(".txtcost"));
    });
    $(".ckNum a.arrowDown").click(function () {
        var $obj = $(this).parents(".ckNum");
        var min = $obj.attr("min");
        var curCount = parseInt($obj.find("label").text());
        if (curCount == parseInt(min)) {
            return true;
        }
        $obj.find("label").text(curCount - 1);
        if (document.getElementById("btnOrder")) {
            //6_4价格跟着数量变化
            var orderPrice = $("#btnOrder").find("label");
            if (reg.price == 0) {
                reg.price = parseFloat(orderPrice.text());
            }
            orderPrice.text(reg.price * (curCount - 1));
        }
        $("#count").val(curCount - 1);
        calculate.minPrice($(this).parents(".bookSolu").find(".txtcost"));
    });
};
reg.uploadImg = function () {
    /// <summary>上传图片</summary>
    $(".btnUpload").click(function (e) {
        if ($(this).attr("verified") && $(this).attr("verified") == "true") {
            return false;
        }
        var src = e.target || window.event.srcElement;
        if ($(src).attr("class") == "imgClose") {
            return false;
        }
        $("input[type='file']").remove();
        var idStr = Date.parse(new Date());
        $("body").append('<input type="file" id="' + idStr + '" class="dn" />');
        var imgObj = $(this);
        $("#" + idStr).click().change(function () {
            imgObj.find(".uploading").remove();
            if ($(this).val() != "") {
                imgObj.append('<div class="uploading"><div>&nbsp;</div></div>')
                reg_ajax.uploadImg(this.files[0], imgObj);
            }
        });
    });
};

reg.appendTag = function (obj) {
    /// <summary>追加标签html</summary>
    var userInput = $.trim($(obj).val());
    if (userInput != "") {
        var isAdd = true;
        var $tagP = $(obj).parents(".tagList");
        $tagP.find("span").each(function () {
            if (userInput == $.trim($(this).text())) {
                alert($(this).text() + "标签已经存在！");
                isAdd = false;
                return false;
            }
        });

        if (isAdd) {
            if ($tagP.find(".tagControl").size() == 0) {
                $tagP.prepend('<div class="tagControl"></div>');
            }
            $tagP.find(".tagControl").append('<span>' + userInput + '</span>');
            $(obj).val("");
            if ($(obj).attr("maxtagcount") && $tagP.find("span").size() >= parseInt($(obj).attr("maxtagcount"))) {
                $(obj).css("display", "none");
            }
        }
    }
};
reg.clickRemoveTag = function (event) {
    /// <summary>点击标签也可以删除</summary>
    if ($(this).attr("isdel") == "false") {
        return false;
    }
    if ($(this).attr("title")) {
        $(this).attr({ "del": "0", "title": "" }).removeClass("cur");
    }
    else {
        $(this).attr({ "del": "1", "title": "按delete键删除标签" }).addClass("cur");
    }

    event.stopPropagation();
};
reg.addTagEvent = function () {
    /// <summary>初始化添加标签的相关事件</summary>
    if ($(".tagList .txt").size() == 0) {
        return;
    }
    $(document).keyup(function (e) {
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if (keycode == 8 || keycode == 46) {
            $(".tagList").find("span.cur").each(function () {
                if ($(this).attr("del") == "1") {
                    $(this).remove();
                }
            });
            $(".tagList").each(function () {
                if ($(this).find("span").size() == 0) {
                    $(this).find(".txt").css("display", "block");
                }
            });
        }
    }).click(function () {
        $(".tagList").find("span.cur").removeClass("cur").attr("del", "0");
    });
    $(".tagList .txt").blur(function () {
        reg.appendTag(this);
    }).keyup(function (e) {
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        var $obj = $(this);
        if (keycode == 13) {
            //如果是用户敲了回车、逗号
            reg.appendTag(this);
        }
        else if (keycode == 32) {
            var inputStr = $(this).val();
            var lastStr = inputStr.substring(inputStr.length - 1);
            if (lastStr != '' && $.trim(lastStr) == '') {
                reg.appendTag(this);
            }
        }
        else if ((keycode == 8 || keycode == 46) && $.trim($(this).val()) == "") {
            //删除

            var delObj = $obj.parents(".tagList").find("span:last");
            if (delObj.is(".cur")) {
                delObj.remove();
            }
            else {
                delObj.addClass("cur");
            }
        }
        if (keycode != 8 && keycode != 46) {
            $(this).parents(".tagList").find("span.cur").removeClass("cur");
        }
    });
    $(document).on("click", ".tagList span", reg.clickRemoveTag);
    $(document).on("click", ".imgClose", function (event) {
        $(this).parents(".btnUpload").find("img").remove();
        $(this).remove();
        event.stopPropagation();
    });
};
reg.imgTab = function () {
    /// <summary>多张上传图片的tab切换卡</summary>
    $(".uploadImgList div").click(function () {

        $(this).parents(".uploadImgList").find("div").each(function () {
            if (!$(this).attr("oldZidx")) {
                $(this).attr("oldZidx", $(this).css("z-index"));
            }
            $(this).css("z-index", $(this).attr("oldZidx"));
        });
        $(this).css("z-index", "700");
    });
};
reg.placeholder = function () {
    /// <summary>模拟浏览器的Placeholder属性</summary>
    $("div.text").keyup(function (e) {
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if ((keycode == 8 || keycode == 46) && $.trim($(this).text()) == "") {
            if ($(this).find(".placeholder").size() == 0) {
                $(this).append('<div class="placeholder">' + $(this).attr("placeholder") + '</div>');
            }
            else {
                $(this).find(".placeholder").css("display", "block");
            }
        }
        else {

            if ($(this).find(".placeholder").css("display") == "block") {
                $(this).text($(this).text().replace($(this).attr("placeholder"), ""));
            }
            $(this).find(".placeholder").css("display", "none");
        }
    });
};
reg.editContent = function (isE) {
    if (isE) {
        $("#btnEdit").text("取消");
        $("#bookCover,.zsUserList,.noEdit").append('<div class="editlayer">&nbsp;</div>');
        var btnObj = $('<li><a class="btn" id="saveEdit" style="width:78px;padding:0;text-align:center; position:relative;top:2px;">保存</a></li>');
        btnObj.click(function () {
            reg_ajax.draft($("#bookCover #btnEdit").attr("projectid"), $(this));
        });
        $("#msgRight").prepend(btnObj);
        $(".picView").css("display", "none");
        $("#editPicView").css("display", "block");
        if (!document.getElementById("editText")) {
            var oldFont = $(".bookTextView").clone();
            oldFont.find(".updateTime,.tt").remove();
            var ht = '<textarea id="editText" class="text" style="height:' + $(".bookTextView").height() + 'px">' + oldFont.text() + '</textarea>';
            $(".bookTextView").before(ht).css("display", "none");
        }
        else {
            $(".bookTextView").css("display", "none");
            $("#editText").css("display", "block");
        }
    }
    else {
        $("#btnEdit").text("编辑");
        $("#bookCover,.zsUserList,.noEdit").find(".editlayer").remove();
        $("#msgRight").find("#saveEdit").parents("li").remove();

        $("#editPicView,#editText").css("display", "none");
        $(".picView,.bookTextView").css("display", "block");


    }

};
reg.cloneAuthor;
reg.addAuthor = function () {
    /// <summary>增加作者编辑栏</summary>
    if ($(".authorMsg").size() == 0) {
        return;
    }
    reg.cloneAuthor = $(".authorMsg").find("ul").clone(true);
    $(".authorMsg a.btnAuthor").click(function () {
        if (reg.cloneAuthor) {
            $(this).before(reg.cloneAuthor.clone(true));
            reg.delAuthor();
        }
    });
    reg.delAuthor();
};
reg.delAuthor = function () {
    /// <summary>删除作者编辑栏</summary>
    $(".authorMsg a.btnDel").click(function () {
        $(this).parents("ul").remove();
    });
};
reg.vCodeObj = null;
reg.curTime = 59;
reg.vCode = function () {
    /// <summary>点击后有1分钟倒计时</summary>
    $("input.btnVCode").click(function () {
        if (!document.getElementById("txtMob") || $.trim($("#txtMob").val()) == "") {
            tip("请先填写手机号码！", $(this));
            return false;
        }
        if (!document.getElementById("txtCaptcha") || $.trim($("#txtCaptcha").val()) == "") {
            tip("请先填写图片验证码！", $(this));
            return false;
        }
        var mobStr = $.trim($("#txtMob").val());
        if (!isMob(mobStr)) {
            tip("所填的手机号码格式不正确！", $(this));
            return false;
        }
        reg.vCodeObj = $(this);
        reg_ajax.verification($.trim($("#txtMob").val()), $.trim($("#txtCaptcha").val()), function () {
            var time = setInterval(function () {
                if (!reg.vCodeObj || reg.curTime < 2) {
                    clearInterval(time);
                    reg.curTime = 59;
                    reg.vCodeObj.attr("disabled", false).val("获取");
                    reg.vCodeObj = null;
                }
                else {
                    reg.curTime--;
                    reg.vCodeObj.attr("disabled", true).val(reg.curTime + "秒后再获取");
                }
            }, 1000);
        }, function (failure) {
            tip(failure, reg.vCodeObj);
        });
    });
    /// <summary>补充注册信息2_1_2_1_3进行校验</summary>
    $("#add_info1").click(function () {
        var obj_password = $(this).parents("form").find("#password");
        var obj_confirmPassword = $(this).parents("form").find("#confirmPassword");
        if (!document.getElementById("name") || $.trim($("#name").val()) == "") {
            tip("请先填写笔名！", $(this));
            return false;
        }
        if ($.trim(obj_password.val()) == "") {
            tip("请先输入密码！", obj_password);
            return false;
        }
        if ($.trim(obj_password.val()).length < 6) {
            tip("密码长度不能少于6位！", obj_password);
            return false;
        }
        if ($.trim(obj_confirmPassword.val()) == "") {
            tip("请先输入确认密码！", obj_confirmPassword);
            return false;
        }
        if ($.trim(obj_password.val()) != $.trim(obj_confirmPassword.val())) {
            tip("两次密码输入不一致！", obj_confirmPassword);
            return false;
        }
        if ($(this).parents("form").find(":checkbox:checked").size() == 0) {
            tip("请先阅读赞赏注册服务协议！", $(this));
            return false
        }
    });
    /// <summary>补充注册信息2_1_2_1_4进行校验</summary>
    $("#add_info2").click(function () {
        var obj_password = $(this).parents("form").find("#password");
        var obj_confirmPassword = $(this).parents("form").find("#confirmPassword");
        if (!document.getElementById("name") || $.trim($("#name").val()) == "") {
            tip("请先填写笔名！", $(this));
            return false;
        }
        if (!document.getElementById("txtMob") || $.trim($("#txtMob").val()) == "") {
            tip("请先填写手机号码！", $(this));
            return false;
        }
        if ($.trim(obj_password.val()) == "") {
            tip("请先输入密码！", obj_password);
            return false;
        }
        if ($.trim(obj_password.val()).length < 6) {
            tip("密码长度不能少于6位！", obj_password);
            return false;
        }
        if ($.trim(obj_confirmPassword.val()) == "") {
            tip("请先输入确认密码！", obj_confirmPassword);
            return false;
        }
        if ($.trim(obj_password.val()) != $.trim(obj_confirmPassword.val())) {
            tip("两次密码输入不一致！", obj_confirmPassword);
            return false;
        }
        if (!document.getElementById("txtCaptcha") || $.trim($("#txtCaptcha").val()) == "") {
            tip("请先填写图片验证码！", $(this));
            return false;
        }
        var mobStr = $.trim($("#txtMob").val());
        if (!isMob(mobStr)) {
            tip("所填的手机号码格式不正确！", $(this));
            return false;
        }
        if (!document.getElementById("code") || $.trim($("#code").val()) == "") {
            tip("请先填写短信验证码！", $(this));
            return false;
        }
        if ($(this).parents("form").find(":checkbox:checked").size() == 0) {
            tip("请先阅读赞赏注册服务协议！", $(this));
            return false
        }
    });
    /// <summary>点击后重置密码后进行校验</summary>
    $("#btn_resetPW").click(function () {
        var obj_password = $(this).parents("form").find("#rawPassword");
        var obj_confirmPassword = $(this).parents("form").find("#confirmPassword");
        if (!document.getElementById("txtMob") || $.trim($("#txtMob").val()) == "") {
            tip("请先填写手机号码！", $(this));
            return false;
        }
        if (!document.getElementById("txtCaptcha") || $.trim($("#txtCaptcha").val()) == "") {
            tip("请先填写图片验证码！", $(this));
            return false;
        }
        var mobStr = $.trim($("#txtMob").val());
        if (!isMob(mobStr)) {
            tip("所填的手机号码格式不正确！", $(this));
            return false;
        }
        if (!document.getElementById("code") || $.trim($("#code").val()) == "") {
            tip("请先填写短信验证码！", $(this));
            return false;
        }
        if (!document.getElementById("rawPassword") || $.trim($("#rawPassword").val()) == "") {
            tip("请先填写新密码！", $(this));
            return false;
        }
        if ($.trim(obj_password.val()).length < 6) {
            tip("密码长度不能少于6位！", obj_password);
            return false;
        }
        if ($.trim(obj_confirmPassword.val()) == "") {
            tip("请先输入确认密码！", obj_confirmPassword);
            return false;
        }
        if ($.trim(obj_password.val()) != $.trim(obj_confirmPassword.val())) {
            tip("两次密码输入不一致！", obj_confirmPassword);
            return false;
        }

    });
};
reg.showNextPage = function (prevNum, nextNum) {
    /// <summary>下一页切换显示效果</summary>
    $("#page" + prevNum).animate({ "margin-left": "-1000px" }, "slow", function () {
        $("#page" + prevNum).css("display", "none");
        $("body").scrollTop(0);
        $("#page" + nextNum).css("display", "block").animate({ "margin-left": "0" }, "normal", function () {
            $("#leftMenu li.cur").removeClass("cur");
            var curObj = $("#leftMenu li").eq(nextNum - 1);
            curObj.addClass("cur");
            $("#leftMenu .curTag").css("top", parseFloat(curObj.position().top) - 8);
        });
    });
};
reg.showPrevPage = function (prevNum, nextNum) {
    /// <summary>上一页切换显示效果</summary>
    $("#page" + nextNum).animate({ "margin-left": "1000px" }, "slow", function () {
        $("#page" + nextNum).css("display", "none");
        $("body").scrollTop(0);
        $("#page" + prevNum).css("display", "block").animate({ "margin-left": "0" }, "normal", function () {
            $("#leftMenu li.cur").removeClass("cur");
            var curObj = $("#leftMenu li").eq(prevNum - 1);
            curObj.addClass("cur");
            $("#leftMenu .curTag").css("top", parseFloat(curObj.position().top) - 8);
        });
    });
};
reg.createTest = function (idx, obj) {
    if (idx == "1") {
        var error = "";
        if ($.trim($("#txtBookName").val()) == "") {
            error = "·请输入书名";
        }
        if ($("#cmenu li.cur").size() == 0) {
            error += "&nbsp;·请选择图书类别！";
        }
        if ($.trim($("#area_description").val()) == "") {
            error += "&nbsp;·请在”简介/描述“一栏里填写图书简介！";
        }
        else if ($.trim($("#area_draft").val()) == "") {
            error = "请填写不少于2000字的部分书稿…";
        }
        if (error != "") {
            tip(error, obj);
            return false;
        }
    }
    return true;
}
reg.initPageShow = function () {
    $("a.btnNext").click(function () {

        var idx = parseInt($(this).index()) + 1;
        reg.showNextPage(idx, idx + 1);
    });
    $("a.btnPrev").click(function () {
        var idx = parseInt($("a.btnPrev").index($(this)[0])) + 1;
        var prIdx = idx;
        if (idx > 1) {
            prIdx = idx - 1;
        }
        reg.showPrevPage(idx, idx + 1);
    });
};
reg.ckMenu = function () {
    $(".menu ul li").click(function () {
        if (!document.getElementById("address")) {
            $("body").append('<input type="hidden" id="address" name="address" />');
        }
        $(this).parents(".menu").find("label").text($(this).text()).css("color", "#191c1c");
        $(this).parents("ul").css("display", "none");
        $("#address").val($(this).text());
    });
    $(".menu a").hover(function () {
        $(this).find("ul").css("display", "block");
    }, function () {
        $(this).find("ul").css("display", "none");
    });
};
reg.ckAll = function () {
    $(":checkbox.ck").click(function () {
        $(":checkbox").not($(this)).prop("checked", $(this).is(":checked"));
    });
};
var editAddrObj = null;
reg.init = function () {
    $("#bookCover #btnEdit").click(function () {
        reg.editContent($.trim($(this).text()) == "编辑");
    });
    reg.uploadImg();
    reg.addTagEvent();
    reg.imgTab();
    reg.placeholder();
    reg.addAuthor();
    reg.vCode();
    reg.initPageShow();
    reg.addNumber();
    reg.setxtHeight();
    reg.ckMenu();
    reg.ckAll();
    reg.edit();
    $("input.number").keyup(function () {
        var inputStr = $(this).val();
        if (!/^\d+[.]?\d*$/.test(inputStr)) {
            $(this).val(/^\d+[.]?\d*/.exec(inputStr));
        }
        return false;
    });
    /*$(document).on("mouseenter", "td.td_addr", function () {
     if (!$(this).attr("addrid")) {
     return false;
     }
     var font = "";
     if ($(this).attr("addr")) {
     font = $(this).attr("addr");
     }
     else {
     font = $(this).text();
     $(this).attr("addr", font);
     }
     var urlStr = $(this).attr("addrurl") + $(this).attr("addrid");
     $(this).html('<div class="shadow">' + font + '<a openUrl="#page7_11" >修改收货地址</a></div>');
     });
     $(document).on("mouseleave", "td.td_addr", function () {

     $(this).html($(this).attr("addr"));

     });*/
};
var sln = {};
sln.copyHtml = null;
sln.add = function () {
    if (!document.getElementById("btnSln")) {
        return false;
    }
    sln.copyHtml = $(".bookSolu:eq(1)").clone(true).css("display", "block");
    sln.copyHtml.find(".btnClose").click(function () {
        $(this).parents(".bookSolu").remove();
        $(".bookSolu").each(function (idx) {
            if (idx == 0) {
                return true;
            }
            if (idx == 1) {
                $(this).find(".tt").text("贰");
            }
            if (idx == 2) {
                $(this).find(".tt").text("叁");
            }
            if (idx == 3) {
                $(this).find(".tt").text("肆");
            }
            calculate.minPrice($(this).find(".txtcost"));
        });
    });
    $(".bookSolu:eq(1)").remove();
    $("#btnSln").click(function () {
        if ($.trim($("#minPrice label").text()) == "") {
            tip("请先在顶部计算出“最低筹款金额”！", $(this));
            return false;
        }
        if (sln.copyHtml) {
            var newObj = sln.copyHtml.clone(true);
            var bookCount = $(".bookSolu").size();
            if (bookCount == 1) {
                newObj.find(".tt").text("贰");
            }
            else if (bookCount == 2) {
                newObj.find(".tt").text("叁");
            }
            else if (bookCount == 3) {
                newObj.find(".tt").text("肆");
            }
            if ($(".bookSolu").size() == 4) {
                tip("最多只有四种方案！", $(this));
                return true;
            }

            $(".inputMoney").before(newObj);
        }
    });
};
sln.init = function () {
    sln.add();
};
var ui = {};

ui.pf = function () {
    /// <summary>设置页脚位置</summary>
    if (document.documentElement.clientHeight <= document.documentElement.offsetHeight + 60) {
        $("#pf").css("position", "relative");
    }
    else {
        $("#pf").css("position", "absolute");
    }
    $("#pf").css("display", "block");
};
ui.scroll = function () {
    $(window).scroll(function () {
        if (document.getElementById("fxTools")) {
            var fhg = parseFloat($(".zsUserList").offset().top);
            if (parseFloat($(window).height() + $("#ph").offset().top) < fhg) {
                $("#fxTools").css("display", "block");
            }

            if (parseFloat($("#fxTools").offset().top) + 100 > fhg) {
                $("#fxTools").css("display", "none");
            }
        }
    });
};
ui.init = function () {
    ui.pf();
    ui.scroll();
    if (document.getElementById("fixTip")) {
        $("#fixTip").css("margin-left", 1 - parseFloat($("#fixTip").css("width")) / 2);
        $("#fixTip .close").click(function () {
            $("#fixTip").remove();
        });
    }
    if (document.getElementById("time")) {
        setInterval(function () {
            var num = parseInt($("#time").text()) - 1;
            if (num === 1) {
                window.location.href = $("#time").parent().next().attr("href")
            }
            if (num > 0) {
                $("#time").text(num);
            }
        }, 1000);

    }
};
var book = {};
book.zsList = function () {
    /// <summary>项目详情</summary>
    $(".zsUserList ul:eq(0)").css("display", "block");
    var ul_hg = 0;
    $(".zsUserList ul").each(function () {
        if (parseFloat($(this).css("height")) > ul_hg) {
            ul_hg = parseFloat($(this).css("height"));
        }
    });
    $(".slnBorder").css("height", ul_hg);
    $(".zsUserList .pagers a").click(function () {
        $(".zsUserList .pagers a").removeClass("btn");
        $(this).addClass("btn");
        $(".zsUserList ul").not("#slnList").css("display", "none").eq($(this).index()).css("display", "block");
    });
};
book.init = function () {
    book.zsList();
};
var salong = {};
//salong.pager = function () {
//    /// <summary>沙龙列表分页</summary>
//    $(".salonList .prev,.salonList .next").click(function () {
//        var typeStr = "my";
//        if ($(".salonList .tt").text().indexOf("全部") != -1) {
//            typeStr = "all";
//        }
//        reg_ajax.salons(parseInt($(this).attr("size")), typeStr, $("#salongCtr"), $(this));
//    });
//};
salong.chat = function () {
    /// <summary>沙龙详情讨论</summary>
    $("#bottomFlx .btn").click(function () {
        var str_chat = $.trim($("#bottomFlx textarea").val());
        if (str_chat == "") {
            tip("请先输入聊天内容！", $(this));
            return false;
        }
        reg_ajax.chat(str_chat, $(this));
    });
};
salong.init = function () {
    //    salong.pager();
    salong.chat();
};
var validate = {
    addr: function () {
        /// <summary>添加地址的前端验证</summary>
        if ($.trim($("#txtName_addr").val()) == "") {
            tip("请先输入姓名！", $("#txtName_addr"));
            return true;
        }
        if ($.trim($("#txtMob_addr").val()) == "") {
            tip("请先输入手机号码！", $("#txtMob_addr"));
            return true;
        }
        if (!isMob($.trim($("#txtMob_addr").val()))) {
            tip("所填手机号格式不正确！", $("#txtMob_addr"));
            return true;
        }
        if ($.trim($("#sls_pro").val()) == "") {
            tip("请先选择省份！", $("#sls_pro"));
            return true;
        }
        if ($.trim($("#sls_city").val()) == "") {
            tip("请先选择城市！", $("#sls_city"));
            return true;
        }
        if ($.trim($("#sls_dist").val()) == "") {
            tip("请先选择区县！", $("#sls_dist"));
            return true;
        }
        if ($.trim($("#txtAddr").val()) == "") {
            tip("请先输入街道地址！", $("#txtAddr"));
            return true;
        }
        if ($.trim($("#txtPost").val()) == "") {
            tip("请先输入邮编！", $("#txtPost"));
            return true;
        }
        return false;
    },
    init: function () {
        $("#btn_vemail").click(function () {
            /// <summary>找回密码时的邮箱验证</summary>
            var str_email = $.trim($("#email").val());
            if (str_email == "") {
                tip("请先填写注册时输入的邮箱！", $("#email"));
                return false;
            }
            if (!isEmail(str_email)) {
                tip("所填邮箱格式不正确！", $("#email"));
                return false;
            }
        });
        $("#div_organ :submit").click(function () {
            /// <summary>机构注册</summary>
            if ($.trim($("#companyName").val()) == "") {
                tip("请先输入机构名称！", $("#companyName"));
                return false;
            }
            if ($.trim($("#companyCode").val()) == "") {
                tip("请先输入组织机构代码！", $("#companyCode"));
                return false;
            }
            if ($.trim($("#contact").val()) == "") {
                tip("请先输入联系人姓名！", $("#contact"));
                return false;
            }
            if ($.trim($("#contactPhone").val()) == "") {
                tip("请先输入联系人电话！", $("#contactPhone"));
                return false;
            }
            var obj_email = $(this).parents("form").find("#email");
            if ($.trim(obj_email.val()) == "") {
                tip("请先输入联系人邮箱！", obj_email);
                return false;
            }
            if (!isEmail($.trim(obj_email.val()))) {
                tip("输入的邮箱格式不正确！", obj_email);
                return false;
            }
            var obj_password = $(this).parents("form").find("#password");
            var obj_confirmPassword = $(this).parents("form").find("#confirmPassword");
            if ($.trim(obj_password.val()) == "") {
                tip("请先输入密码！", obj_password);
                return false;
            }
            if ($.trim(obj_password.val()).length < 6) {
                tip("密码长度不能少于6位！", obj_password);
                return false;
            }
            if ($.trim(obj_confirmPassword.val()) == "") {
                tip("请先输入确认密码！", obj_confirmPassword);
                return false;
            }
            if ($.trim(obj_password.val()) != $.trim(obj_confirmPassword.val())) {
                tip("两次密码输入不一致！", obj_confirmPassword);
                return false;
            }
            if ($("#upload_license img").size() == 0) {
                tip("请先上传营业执照电子版照片！", $("#upload_license"));
                return false;
            }
            if ($(this).parents("form").find(":checkbox:checked").size() == 0) {
                tip("请先阅读赞赏注册服务协议！", $(this));
                return false
            }
        });
        /// <summary>个人注册（手机注册）</summary>
        $("#btn_reg_pone").click(function(){
            var obj_pon=$(this).parents("form").find("#phone");
            if (!document.getElementById("phone") || $.trim($("#phone").val()) == "") {
                tip("请填写手机号码！", obj_pon);
                return false;
            }
            var obj_phone = $.trim($("#phone").val());
            if (!isMob(obj_phone)) {
                tip("所填的手机号码格式不正确！", obj_pon);
                return false;
            }
            if (!document.getElementById("name") || $.trim($("#name").val()) == "") {
                tip("请先填写笔名！", obj_pon);
                return false;
            }
            var obj_password = $(this).parents("form").find("#password");
            var obj_confirmPassword = $(this).parents("form").find("#confirmPassword");
            if ($.trim(obj_password.val()) == "") {
                tip("请先输入密码！", obj_password);
                return false;
            }
            if ($.trim(obj_password.val()).length < 6) {
                tip("密码长度不能少于6位！", obj_password);
                return false;
            }
            if ($.trim(obj_confirmPassword.val()) == "") {
                tip("请先输入确认密码！", obj_confirmPassword);
                return false;
            }
            if ($.trim(obj_password.val()) != $.trim(obj_confirmPassword.val())) {
                tip("两次密码输入不一致！", obj_confirmPassword);
                return false;
            }
            var obj_yzm=$(this).parents("form").find("#txtCaptcha");
            if (!document.getElementById("txtCaptcha") || $.trim($("#txtCaptcha").val()) == "") {
                tip("请先填写图片验证码！", obj_yzm);
                return false;
            }
            if (!document.getElementById("code") || $.trim($("#code").val()) == "") {
                tip("请先填写短信验证码！", obj_yzm);
                return false;
            }
            if ($(this).parents("form").find(":checkbox:checked").size() == 0) {
                tip("请先阅读赞赏注册服务协议！", $(this));
                return false
            }
        });
        /*手机注册获取验证码*/
        $(".btn_getYzm").click(function(){
            var obj_pon=$(this).parents("form").find("#phone");
            if (!document.getElementById("phone") || $.trim($("#phone").val()) == "") {
                tip("请填写手机号码！", obj_pon);
                return false;
            }
            var obj_yzm=$(this).parents("form").find("#txtCaptcha");
            if (!document.getElementById("txtCaptcha") || $.trim($("#txtCaptcha").val()) == "") {
                tip("请先填写图片验证码！", obj_yzm);
                return false;
            }
            var obj_phone = $.trim($("#phone").val());
            if (!isMob(obj_phone)) {
                tip("所填的手机号码格式不正确！", $(this));
                return false;
            }
            reg.vCodeObj = $(this);
            reg_ajax.verification($.trim($("#phone").val()), $.trim($("#txtCaptcha").val()), function () {
                var time = setInterval(function () {
                    if (!reg.vCodeObj || reg.curTime < 2) {
                        clearInterval(time);
                        reg.curTime = 59;
                        reg.vCodeObj.attr("disabled", false).text("获取");
                        reg.vCodeObj = null;
                    }
                    else {
                        reg.curTime--;
                        reg.vCodeObj.attr("disabled", true).text(reg.curTime + "秒后再获取");
                    }
                }, 1000);
            }, function (failure) {
                tip(failure, reg.vCodeObj);
            });
        });

        $("#btn_reg_pro").click(function () {
            /// <summary>个人注册</summary>
            var obj_email = $(this).parents("form").find("#email");
            if ($.trim(obj_email.val()) == "") {
                tip("请先输入邮箱！", obj_email);
                return false;
            }
            if (!isEmail($.trim(obj_email.val()))) {
                tip("输入的邮箱格式不正确！", obj_email);
                return false;
            }
            var obj_name = $(this).parents("form").find("#name");
            if ($.trim(obj_name.val()) == "") {
                tip("请先输入笔名！", $("#name"));
                return false;
            }
            var obj_password = $(this).parents("form").find("#password");
            var obj_confirmPassword = $(this).parents("form").find("#confirmPassword");
            if ($.trim(obj_password.val()) == "") {
                tip("请先输入密码！", obj_password);
                return false;
            }
            if ($.trim(obj_password.val()).length < 6) {
                tip("密码长度不能少于6位！", obj_password);
                return false;
            }
            if ($.trim(obj_confirmPassword.val()) == "") {
                tip("请先输入确认密码！", obj_confirmPassword);
                return false;
            }
            if ($.trim(obj_password.val()) != $.trim(obj_confirmPassword.val())) {
                tip("两次密码输入不一致！", obj_confirmPassword);
                return false;
            }
            if ($(this).parents("form").find(":checkbox:checked").size() == 0) {
                tip("请先阅读赞赏注册服务协议！", $(this));
                return false
            }
        });
    }
};
var data = {};
data.setAddress = function () {
    $("#btnSaveAddr,#btnSaveDefAddr,#btnNewAddr").click(function () {
        if (validate.addr()) {
            return false;
        }
        var str_address = $("#sls_pro option:selected").text()
            + $("#sls_city option:selected").text()
            + $("#sls_dist option:selected").text() + ", " + $("#txtAddr").val();
        if ($(this).is("#btnNewAddr")) {
            //未登录的用户设置地址不需要后台交互
            str_address = $.trim($("#txtName_addr").val()) + "," + $.trim($("#txtMob_addr").val()) + "," + str_address + "," + $.trim($("#txtPost").val());
            if (!document.getElementById("address")) {
                $("body").append('<input type="hidden" id="address" name="address"  />');
            }
            $("#newAddr label").text(str_address);
            $(this).parents(".win").hide();
            $(".btnRight").css("display", "none");
            $("body").css("overflow-y", "auto");
            $("#newAddr").show();
            $("#address").val(str_address);

        }
        else {
            reg_ajax.setAddress("true", $.trim($("#txtName_addr").val()), $.trim($("#txtMob_addr").val()), $.trim($("#txtPost").val()), str_address, $.trim($("#return").val()), $(this));
        }

    });
    $("#btnSaveAddr2,#btnSaveDefAddr2").click(function () {
        if (validate.addr()) {
            return false;
        }
        var str_address = $("#sls_pro option:selected").text()
            + $("#sls_city option:selected").text()
            + $("#sls_dist option:selected").text() + ", " + $("#txtAddr").val();
        reg_ajax.address(str_address, $.trim($("#txtPost").val()), $.trim($("#txtName_addr").val()), $.trim($("#txtMob_addr").val()), $(this), editAddrObj.attr("addrid"));
    });
};
data.setDefAddress = function () {
    $(".addrList .defAddr").click(function () {
        reg_ajax.setDefAddress($(this).attr("addressid"), $(this));
    });
};
data.pro = function () {
    /// <summary>添加省份数据</summary>
    var ht = '';
    if (!document.getElementById("sls_pro")) { return false; }
    for (var s = 1; s < 35; s++) {
        for (var p in pro) {
            if (parseInt(pro[p].ProSort) == s) {
                ht += '<option value="' + pro[p].ProID + '">' + pro[p].name + '</option>';
                break;
            }
        }
    }
    $("#sls_pro").append(ht).change(data.city);
};
data.city = function () {
    var proid = parseInt($(this).val());
    var ht = '';
    var fc = 0, cityId = 0;
    for (c in citys) {
        if (proid == parseInt(citys[c].ProID)) {
            if (fc == 0) {
                cityId = citys[c].CityID;
            }
            fc++;
            ht += '<option value="' + citys[c].CityID + '">' + citys[c].name + '</option>';
        }
    }

    $("#sls_city").empty().append(ht).change(function () {
        data.cityid = $(this).val();
        data.dist();
    });
    data.cityid = cityId;
    data.dist();
};
data.cityid = 0;
data.dist = function () {
    var ht = '';
    for (c in dist) {
        if (data.cityid == parseInt(dist[c].CityID)) {
            ht += '<option>' + dist[c].DisName + '</option>';
        }
    }
    $("#sls_dist").empty().append(ht);
};
data.bid = function () {
    $(".inputPrice .btn").click(function () {
        if ($.trim($(".inputPrice .txt").val()) == "") {
            tip("请先输入报价！", $(".inputPrice .txt"));
            return false;
        }
        reg_ajax.bid($(this).attr("projectid"), $(".inputPrice .txt").val(), $(this));
    });
};
var stopAjax;
data.statusAjax = function () {
    if (document.getElementById("ajax_paymentid")) {
        stopAjax = setInterval(function () {
            reg_ajax.statusAjax($("#ajax_paymentid").val());
        }, 1000);
    }
};
data.read = function () {
    $(".logList .btnRight .btn").click(function () {
        var idsObj = $("td :checkbox:checked");
        if (idsObj.size() == 0) {
            tip("请先勾选要标记的数据！", $(this));
            return false;
        }
        reg_ajax.read(idsObj, $(this));
    });
};
data.getprojects = function () {
    $("#projects .more").click(function () {
        reg_ajax.getprojects($(this).attr("size"), $(this));
    });
};
data.getprojSData = function () {
    var userData = {};
    userData.name = $.trim($("#txtBookName").val());
    var typeStr = [];
    $("#ckTag li").each(function () {
        typeStr.push($.trim($(this).text()));
    });
    userData.types = typeStr;
    userData.description = $("#area_description").val();
    userData.cover = $(".bookPic img").attr("src");
    if (userData.cover) {
        userData.cover = userData.cover.replace(reg_ajax.imgServer, "");
    }
    else {
        userData.cover = '';
    }
    userData.aboutFirstAuthor = $("#lab_aboutFirstAuthor").val();
    userData.outline = $("#area_outline").val();
    userData.draft = $("#area_draft").val();
    userData.color = $("input[name='rd_print']:checked").size() > 0 ? $("input[name='rd_print']:checked").val() : "0";
    userData.publishMode = $("input[name='rd_ver']:checked").size() > 0 ? $("input[name='rd_ver']:checked").val() : "0";
    userData.wordCount = $.trim($("#txtwordCount").val()) == "" ? "0" : $.trim($("#txtwordCount").val());
    userData.imageCount = $.trim($("#txtimageCount").val()) == "" ? "0" : $.trim($("#txtimageCount").val());
    userData.images = [];
    $(".uploadImgList .btnUpload img").each(function () {
        userData.images.push($(this).attr("src").replace(reg_ajax.imgServer, ""));
    });
    userData.authors = [];
    $(".authorMsg ul").each(function () {
        var newAuthor = {};
        newAuthor.name = $.trim($(this).find("li:eq(0)").find("span:eq(0)").text());
        newAuthor.identity = $.trim($(this).find("li:eq(1)").find("span:eq(0)").text());
        newAuthor.description = $.trim($(this).find("textarea").val());
        if (newAuthor.name == "" && newAuthor.identity == "" && newAuthor.description == "") {
            return true;
        }
        userData.authors.push(JSON.stringify(newAuthor));
    });
    userData.tags = [];
    $(".uploadImgList").prevAll(".tagList").find("span").each(function () {
        if ($.trim($(this).text()) != "") {
            userData.tags.push($.trim($(this).text()));
        }
    });

    userData.authorId = $.trim($("#txtId").val());
    userData.authorName = $.trim($("#txtName").val());
    if ($("#imgId img").size() == 0) {
        userData.authorIdFront = "";
    }
    else {
        userData.authorIdFront = $("#imgId img").attr("src").replace(reg_ajax.imgServer, "");
    }
    if ($("#imgIdBack img").size() == 0) {
        userData.authorIdBack = "";
    }
    else {
        userData.authorIdBack = $("#imgIdBack img").attr("src").replace(reg_ajax.imgServer, "");
    }
    return userData;
};
data.addprojects = function () {
    /// <summary>创建项目</summary>
    $("#btnAddAuthor").click(function () {
        if ($.trim($("#txtName").val()) == "") {
            alert("请填写您的真实姓名！");
            return false;
        }
        if ($.trim($("#txtId").val()) == "") {
            alert("请填写您的身份证号码！");
            return false;
        }
        if ($("#imgId img").size() == 0) {
            alert("请点击“身份证正面”框上传您的身份证正面照！");
            return false;
        }
        if ($("#imgIdBack img").size() == 0) {
            alert("请点击“身份证背面”框上传您的身份证背面照！");
            return false;
        }
        $("#btnLastSave").attr("isopen", false);
        var saveData = data.getprojSData();
        reg_ajax.addprojects(saveData, $(this));
    });
    $("#btnLastSave").click(function () {
        if ($(this).attr("id") == "btnLastSave" && $(this).attr("isopen") && $(this).attr("isopen") == "true") {
            layer2.open("#page5_6");
        }
        else {
            var saveData = data.getprojSData();

            reg_ajax.addprojects(saveData, $(this));
        }
    });
    $("#tempSave").click(function () {
        var saveData = data.getprojSData();
        reg_ajax.previewprojects(saveData, $(this));
    });
};
data.item_orders = function () {
    $("#item_orders .prev,#item_orders .next").click(function () {
        reg_ajax.orders(parseInt($(this).attr("size")), $(this));
    });
    $("#item_projects .prev,#item_projects .next").click(function () {
        reg_ajax.myprojects(parseInt($(this).attr("size")), $(this));
    });
    $("#item_published .prev,#item_published .next").click(function () {
        reg_ajax.item_published(parseInt($(this).attr("size")), $(this));
    });

};
data.init = function () {
    //    data.item_orders();
    data.setAddress();
    data.pro();
    data.setDefAddress();
    data.bid();
    data.statusAjax();
    data.read();
    data.addprojects();
    data.getprojects();
};

var setData = {};
setData.companyPhone = function () {
    /// <summary>7.2 联系人手机号</summary>
    function testData() {
        var mobStr = $.trim($("#txtMob").val());
        if (mobStr == "") {
            tip("手机号码不能为空！", $("#txtMob"));
            return false;
        }
        if (!isMob(mobStr)) {
            tip("所填手机号格式不正确！", $("#txtMob"));
            return false;
        }
        if ($.trim($("#txtCode").val()) == "") {
            tip("请输入手机获取到的验证码！", $("#txtMob"));
            return false;
        }
        return true;
    }
    $("#set_companyPhone").click(function () {
        if (testData()) {
            setting.companyPhone($.trim($("#txtCode").val()), $.trim($("#txtMob").val()), $(this));
        }
    });
    $("#set_personalPhone").click(function () {
        if (testData()) {
            setting.personalPhone($.trim($("#txtCode").val()), $.trim($("#txtMob").val()), $(this));
        }
    });
};
setData.init = function () {
    setData.companyPhone();
    $("#btnSlnData").click(function () {
        var minGoal = $("#minPrice label").text();
        var goal = $("#goal").val();
        if (Number(goal) < Number(minGoal)) {
            alert("最终筹款金额不能小于" + minGoal + "元");
            return;
        }
        var ht = '';
        $(".bookSolu").each(function (idx) {
            if ($(this).css("display") == "block") {
                var bookCount = 1;
                if ($(this).find(".ckNum label").size() > 0) {
                    bookCount = parseInt($(this).find(".ckNum label").text());
                }
                ht += '<input type="hidden" name="rewards[' + idx + '].bookCount" value="' + bookCount + '" />';
                var other = "";
                if ($(this).find(".addOther textarea").size() > 0) {
                    other = $(this).find(".addOther textarea").val();
                }
                ht += '<input type="hidden" name="rewards[' + idx + '].other" value="' + other + '" />';
                if ($(this).find(".txtprice").size() > 0) {
                    ht += '<input type="hidden" name="rewards[' + idx + '].price" value="' + $(this).find(".txtprice").val() + '" />';
                }
                var cost = "";
                if ($(this).find(".txtcost").size() > 0) {
                    cost = $(this).find(".txtcost").val();
                }
                ht += '<input type="hidden" name="rewards[' + idx + '].cost" value="' + cost + '" />';
            }
        });
        ht += '<input type="hidden" name="rewards[0].price" value="' + $("#setPrice .txt").val() + '" />';
        $("#hd_list").html(ht);
        $(this).val("提交中…");
        $("#btnSave").trigger("click");
    });
    $("#set_publisherApply").click(function () {
        /// <summary>7.2申请成为出版社</summary>
        setting.publisherApply($(this));
    });
    $("#set_Name").click(function () {
        /// <summary>7.1 里的笔名 7.2里的联系人名字</summary>
        if ($.trim($("#txtName").val()) == "") {
            tip("", $("#txtName"));
            return false;
        }
        setting.name($.trim($("#txtName").val()), $(this));
    });
    $("#set_email").click(function () {
        /// <summary>绑定邮箱</summary>
        var emailStr = $.trim($("#txtEmail").val());
        if (emailStr == "") {
            tip("要绑定的邮箱不能为空！", $("#txtEmail"));
            return false;
        }
        if (!isEmail(emailStr)) {
            tip("所填邮箱格式不正确！", $("#txtEmail"));
            return false;
        }
        setting.mails(emailStr, $(this));
    });
    $("#set_password").click(function () {

        if ($.trim($("#txtOldPwd").val()) == "") {
            tip("请先输入原密码！", $("#txtOldPwd"));
            return false;
        }
        if ($.trim($("#txtNewPwd").val()) == "") {
            tip("要设置的新密码不能为空！", $("#txtOldPwd"));
            return false;
        }
        if ($.trim($("#txtNewPwd").val()).length < 6 || $("#txtNewPwd").val().length > 20) {
            tip("密码长度为6-20位字符！", $("#txtOldPwd"));
            return false;
        }
        if ($.trim($("#txtNewPwd").val()) != $.trim($("#txtNewPwd2").val())) {
            tip("两次密码输入不一致！", $("#txtOldPwd"));
            return false;
        }
        setting.password($.trim($("#txtOldPwd").val()), $.trim($("#txtNewPwd").val()), $(this));
    });
    $(".addrList .del").click(function () {
        setting.delAddr($(this).attr("addressid"), $(this));
    });
    $("#set_code").click(function () {
        setting.companyCode($.trim($("#txtCode").val()), $(this));
    });
    $("#set_legalname").click(function () {
        if ($.trim($("#txtlegalname").val()) == "") {
            tip("", $("#txtlegalname"));
            return false;
        }
        setting.legalname($.trim($("#txtlegalname").val()), $(this));
    });
    $("#set_qq").click(function () {
        if ($.trim($("#txtqq").val()) == "") {
            tip("QQ号不能为空！", $("#txtqq"));
            return false;
        }
        setting.qq($.trim($("#txtqq").val()), $(this));
    });
    $("#set_identity").click(function () {
        if ($.trim($("#txtidentity").val()) == "") {
            tip("所填身份证号不能为空！", $("#txtidentity"));
            return false;
        }
        setting.identity($.trim($("#txtidentity").val()), $(this));
    });
    $("#btnMessage").click(function () {
        if ($.trim($("#txtMessage").val()) == "") {
            tip("请先输入聊天内容！", $("#txtMessage"));
            return false;
        }
        reg_ajax.message($("#txtMessage").attr("profileid"), $("#txtMessage").val(), $(this));
    });
    $("#btnTopic").click(function () {
        var ht = '';
        $(".btnUpload img").each(function (idx) {
            ht += '<input type="hidden" value="' + $(this).attr("src").replace(reg_ajax.imgServer, "") + '" name="images[]" id="images[]"/>';
        });
        $("form").append(ht);
        $("#sbtTopic").click();
    });
};

var dataTest = {};
dataTest.init = function () {
    //登录验证
    $("#btn_login").click(function () {
        if ($.trim($("#txt_username").val()) == "") {
            tip("邮箱/手机号码不能为空！", $("#txt_username"));
            return false;
        }
        if ($.trim($("#txt_password").val()) == "") {
            tip("登录密码不能为空！", $("#txt_password"));
            return false;
        }
    });

    $("#btnOrder").click(function () {
        if($("#orderEmail").size() != 0) {
            var str_email = $.trim($("#orderEmail .txt").val());
            var address = $.trim($("#address").val());
            if (str_email == "") {
                tip("请输入邮箱！", $("#orderEmail .txt"));

                return false;
            }
            if (!isEmail(str_email)) {
                tip("输入的邮箱格式不正确！", $("#orderEmail .txt"));
                return false;
            }
            if (address == "") {
                tip("请填写地址！", $("#orderEmail .txt"));

                return false;
            }
            if ($(this).parents().siblings().find(":checkbox:checked").size() == 0) {
                tip("请先阅读赞赏注册服务协议！",$("#orderEmail .txt") );
                return false
            }
        }
        if (parseFloat($("#btnOrder label").text()) >= 3000) {
            layer.confirm('大额支付可能会受到微信或支付宝的限额限制，请支付超限用户使用支付宝网页-网银U盾支付。<br /><br /><a style="color:#7a77c8;" href="/support/paymentflow" target="_blank">点击查看支付宝网银U盾付款流程演示</a>'
                , {"title": "重要提示", "btn": ["我知道了"]}, function () {
                    //正式提交的方法放在这里面
                    $('#form')[0].submit();
                });
        } else {
            $('#form')[0].submit();
        }
    });
};

function words() {
    $(".noEdit p,.update p,.pre").each(function () {
        var newWord = "<p>" + $.trim($(this).text()).replace(/\s{3,}/g, "</p><p>") + "</p>";
        $(this).html(newWord);
    });
}
function loadImg() {
    if ($('.imgView').size() > 0 && $('.imgView img').size() > 1) {
        var slideObj = {};
        if (document.getElementById("bookDetails")) {
            slideObj.width = 650;
            slideObj.height = 340;
        }
        else if (document.getElementById("slTime")) {
            slideObj.width = 580;
            slideObj.height = 290;
        }

        slideObj.play = {
            active: true,
            auto: true,
            interval: 5000
        }
        $('.imgView').slidesjs(slideObj);

    }
    else {
        $('.imgView').css("display", "block");
    }
};
function txt_focus() {
    /// <summary>页面上有些文本框默认加上光标，让用户知道哪里可输入</summary>
    $(".inputPrice .txt,#txtBookName").focus();
};
$(function () {
    //    words();
    validate.init();
    tab.init();
    layer2.init();
    reg.init();
    sln.init();
    ui.init();
    book.zsList();
    salong.init();
    data.init();
    setData.init();
    if ($('.imgView').size() > 0 && $('.imgView img').size() > 1) {
        var loadImgTime = setInterval(function () {
            if ($('.imgView img').eq(0)[0].complete) {
                loadImg();
                clearInterval(loadImgTime);
            }
        }, 200);
    }

    $("input[type='text']").attr("autoComplete", "off");
    if (document.getElementById("leftMenu")) {
        window.onbeforeunload = function () {
            return "正在编辑的数据还未保存，是否继续？";
        };
    }
    if (document.getElementById("zsVersion")) {
        $("#zsVersion :radio").click(function () {
            if ($(this).parents("label").text().indexOf("精") != -1) {
                $(this).parents("ul").find("li:last").css("visibility", "visible");
            }
            else {

                $(this).parents("ul").find("li:last").css("visibility", "hidden");
            }
        });
    }
    dataTest.init();
    calculate.init();
    $("#yzm").click(function () {
        $(this).find("img").attr("src", $(this).find("img").attr("src"));
        return false;
    });
    $("#cmenu li").click(function () {
        if ($(this).is(".cur")) {
            $(this).removeClass("cur");
            var delVal = $.trim($(this).text());
            $("#ckTag li").each(function () {
                if ($(this).text() == delVal) {
                    $(this).remove();
                    return false;
                }
            });
        }
        else {
            if ($("#ckTag li").size() > 4) {
                tip("最多只能选择5个类别！", $("#ckTag"));
                return false;
            }
            $(this).addClass("cur");
            $("#ckTag").append('<li>' + $.trim($(this).text()) + '</li>');
        }
    });

    $("#btn_editAddr").click(function () {
        if ($("[name='addr']:checked").size() == 0) {
            tip("请先选择一个地址！", $(this));
            return false;
        }
        reg_ajax.editAddress(editAddrObj.attr("addrid"), $(this), $("[name='addr']:checked").next().text());
    });
    txt_focus();
});
$(window).resize(function () {
    ui.pf();
});