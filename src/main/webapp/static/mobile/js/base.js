function isEmail(szMail) {
    var szReg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var bChk = szReg.test($.trim(szMail));
    return bChk;
}
function isMob(mob) {
    return $.trim(mob).length === 11;
}
var menu = {};
menu.show = function () {
    /// <summary>底部菜单</summary>
    $("#open").click(function () {
        $(this).hide();
        $("#rMenu").show();
    });
};
menu.hide = function () {
    $("#rMenu .slide").click(function () {

        $("#rMenu").hide();
        $("#open").show();
    });
};
menu.init = function () {
    menu.show();
    menu.hide();
};
function alert(error) {
    if ($("#tip").size() == 0) {
        $("body").append('<div id="tip"><div id="vcenter"><div></div></div></div>');
    }
    $("#vcenter div").html(error);
    $("#tip").fadeIn("normal");
    var ct2 = setTimeout(function () {
        clearTimeout(ct2);
        $("#tip").hide();
    }, 2000);
}
/*错误提示对象*/
var messageBox = {
    show: function (font) {
        /// <summary>显示错误提示</summary>
        /// <param name="font" type="String">要提示的文字</param>
        $("#vcenter div").html(font);
        $("#tip").fadeIn("normal");
        var ct2 = setTimeout(function () {
            clearTimeout(ct2);
            $("#tip").hide();
        }, 2000);
    }
};
var tmpl = {
    getprojects: function (data) {
        /// <summary>项目列表页</summary>
        var ht = '<div class="author"><div class="picBorder">';
        var sucNum = Math.round(parseFloat(data.currentMoney) / parseFloat(data.goal) * 100);
        ht += '<a href="' + server + 'projects/' + data.projectId + '"><img src="' + data.cover + '"></a>';
        ht += '</div><h3 class="bookName"><a href="' + server + 'projects/' + data.projectId + '">' + data.bookName + '</a></h3>';
        ht += '<ul class="bookState">';
        if (sucNum >= 100) {
            ht += '<li class="suc progress">';
        }
        else {
            ht += '<li class="progress">';
        }
        ht += sucNum + '%<label>已筹</label></li>';
        if (data.passedDays >= data.totalDays) {
            ht += '<li class="suc time">';
        }
        else {
            ht += '<li class="time">';
        }
        ht += data.passedDays + ' / ' + data.totalDays;
        ht += '<label>进行天数</label></li>';
        if (data.publisherLocked) {
            ht += '<li class="suc">已支持';
        }
        else {
            ht += '<li>/';
        }
        ht += '<label>出版支持</label></li></ul></div>';
        return ht;

    }
};
var tab = {};
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
};
var reg_ajax = {
    state: {
        "FUNDING": "筹款中",
        "PUBLISHING": "出版社出版中",
        "DELIVERING": "物流发货中",
        "SUCCESS": "项目成功",
        "REFUNDING": "退款中",
        "FAILURE": "项目失败",
        "REVIEWING": "审核中"
    }
,
    btn: function (font) {
        /// <summary>修改按钮文字及判断提交重复</summary>
        if ($btnObj.text().indexOf("…") != -1) {
            return true;
        }
        font = font || "提交中…";
        if (!$btnObj.attr("old")) {
            var oldWd = $btnObj.width();
            $btnObj.attr("old", $btnObj.text()).css({ "width": "auto", "min-width": oldWd });
        }
        $btnObj.text(font);
        return false;
    }, isuc: function (data) {
        /// <summary>判断是否请求成功</summary>
        if (!$btnObj.attr("notip")) {
            $btnObj.text($btnObj.attr("old"));
        }
        if (data.code == "ok") {
            if ($btnObj.attr("class") == "btnSmall btnHide") {
                $btnObj.parents(".rTools").find(".btnCancel").click();
            }
            return true;
        }
        reg_ajax.error(data);
        return false;
    }, getPage: function () {
        /// <summary>依据上一次页码返回当前页码</summary>
        var p = parseInt($btnObj.attr("page"));
        if ($btnObj.is(".prev")) {
            p = p - 1;
        }
        else {
            p = p + 1;
        }
        if (p < 0) {
            return 0;
        }
        return p;
    }, setPage: function (data, page) {
        /// <summary>修改当前页码值</summary>
        $btnObj.removeClass("noData");
        if ($btnObj.is(".prev")) {
            var cur = parseInt($btnObj.next().attr("page"));
            if (cur > 0) {
                $btnObj.next().attr("page", cur - 1);
            }
            else {
                $btnObj.next().addClass("noData");
            }
        }
        else {
            var cur = parseInt($btnObj.prev().attr("page"));
            $btnObj.prev().attr("page", cur + 1);
        }
        $btnObj.attr("page", page);
        if ($btnObj.is(".prev") && !data.previous) {
            $btnObj.addClass("noData");
        }
        else if (!data.next) {
            $btnObj.addClass("noData");
        }
    }
};
var server = server || "http://123.57.55.168/zanshang/test/";
reg_ajax.verification = function (phone, captcha, success) {
    /// <summary>手机验证码</summary>
    $.post(server + "phone/verification", { "phone": phone, "captcha": captcha }, function (data) {
        if (data.code == "ok") {
            success();
        } else {
            if (data.result) {
                messageBox.show(data.result);
            }
        }
    }, "json");
};
reg_ajax.verification1 = function (phone, captcha, success) {
    /// <summary>手机验证码</summary>
    $.post(server + "phone/verification1", { "phone": phone }, function (data) {
        if (data.code == "ok") {
            success();
        } else {
            if (data.result) {
                messageBox.show(data.result);
            }
        }
    }, "json");
};
reg_ajax.error = function (data) {
    /// <summary>错误处理</summary>
    if (data.code && data.result) {
        messageBox.show(data.result);
        return false;
    }
    messageBox.show("有可能是网络阻塞或系统异常，请稍后重试！");
};
reg_ajax.setDefAddress = function (addressId, btnObj) {
    /// <summary>设置默认地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/addresses/" + addressId, { "_method_": "PUT" }, function (data) {
        if (reg_ajax.isuc(data)) {
            messageBox.show("设置成功！");
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.setAddress = function (defaultStr, recipient, telephone, postCode, address, retUrl, btnObj) {
    /// <summary>添加地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/addresses", { "default": defaultStr, "recipient": recipient, "telephone": telephone
    , "postCode": postCode, "address": address, "return": retUrl
    }, function (data) {
        if (reg_ajax.isuc(data)) {
            if (document.getElementById("isMob")) {
                var str_newAddr=recipient + "," + telephone + "," + address + "," + postCode;
                $("#addrList").append('<li><label><input type="radio" name="addr" /><span>' + str_newAddr + '</span></label></li>');
                $("#win,#win2").hide();
                clickOpen_obj.prev().text(str_newAddr);

            }
            else if (data.result) {
                window.location = data.result;
            } else {
                messageBox.show("添加成功");
            }
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.getprojects = function (size, btnObj) {
    $btnObj = btnObj;
    if (reg_ajax.btn("加载中…")) {
        return false;
    }
    var page = parseInt(btnObj.attr("page")) + 1;
    $.get(server + "projects", { "page": page, "size": size }, function (data) {
        if (reg_ajax.isuc(data)) {
            btnObj.attr("page", page);
            var datas = data.result.content;
            var ht = '';
            for (var d in datas) {
                ht += tmpl.getprojects(datas[d]);
            }
            btnObj.before(ht);
        }

    }, "json").error(reg_ajax.error);
};
reg_ajax.salons = function (size, type, btnObj) {
    /// <summary>沙龙列表页</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("加载中…")) {
        return false;
    }
    var ajaxUrl = server + "salons";
    if (type == "all") {
        ajaxUrl = server + "salons/all";
    }
    var page = parseInt(btnObj.attr("page")) + 1;
    $.get(ajaxUrl, { "page": page, "size": size }, function (data) {
        if (reg_ajax.isuc(data)) {
            btnObj.attr("page", page);
            var datas = data.result.content;
            var ht = '';
            for (var d in datas) {
                d = datas[d];
                var chatCount = d.chat;
                if (parseInt(d.chat) >= 1000) {
                    chatCount = "1000+";
                }

                ht += '<div class="author">';
                ht += '<a href=""' + d.salonId + '""><img src="' + d.image + '"></a>';
                ht += '<h3 class="bookName"><a href=""' + d.salonId + '"">' + d.name + '</a></h3>';
                ht += '<ul class="salong">';
                ht += '<li>' + d.members + '人参与</li><li class="line">|</li>';
                ht += '<li>' + d.history + '个历史话题</li><li class="line">|</li>';
                ht += '<li>' + chatCount + '条留言</li>';
                ht += '</ul></div>';
            }
            $btnObj.before(ht);
        }
    }, "json").error(reg_ajax.error);

};
reg_ajax.message = function (profileid, message, btnObj) {
    /// <summary>10.1 聊天对话</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "profile/" + profileid + "/message", { "message": message }, function (data) {
        if (reg_ajax.isuc(data)) {
            var pobj = btnObj.parents(".inputMsg");
            var ht = '<li class="my"><img src="' + pobj.find("img").attr("src") + '">\
            <div class="msgC"><p>' + message + '</p>\
            <div class="msgName">我，<label>刚刚</label></div> </div></li>\
            \
            ';
            pobj.find("textarea").val("");
            $(".msgList").prepend(ht);
            $("body,html").scrollTop($("#pLMsg").offset().top);
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.chat = function (chat, btnObj) {
    /// <summary>沙龙详情发送消息</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("发送中…")) {
        return false;
    }
    $.post(server + "salons/" + btnObj.attr("salonid") + "/chat", { "chat": chat }, function (data) {
        if (reg_ajax.isuc(data)) {
            var pobj = btnObj.parents(".inputMsg");
            var ht = '<li class="my"><img src="' + pobj.find("img").attr("src") + '">\
            <div class="msgC"><p>' + chat + '</p>\
            <div class="msgName">我，<label>刚刚</label></div> </div></li>\
            \
            ';
            pobj.find("textarea").val("");
            $(".msgList").prepend(ht);
            $("body,html").scrollTop($("#pLMsg").offset().top);
        }
    }, "json").error(reg_ajax.error);
};
/*2015-09-21*/
reg_ajax.editAddress = function (orderId, btnObj, address) {
    /// <summary>8-1修改收件地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "profile/orders/" + orderId + "/address", { "_method_": "PUT", "address": address }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("修改成功！");
            $("#win,#win2").hide();
            clickOpen_obj.prev().text(address);
            window.location.reload();
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.addOrderAddress = function (orderId, defaultStr, recipient, telephone, postCode, address, btnObj) {
    /// <summary>添加地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "profile/orders/" + orderId + "/address", { "default": defaultStr, "recipient": recipient, "phone": telephone
        , "postCode": postCode, "address": address
    }, function (data) {
        if (reg_ajax.isuc(data)) {
            window.location.reload();
        }
    }, "json").error(reg_ajax.error);
};
//reg_ajax.setComments = function (content, contact) {
//    /// <summary>意见的提交</summary>
//    $.post(server + "suggestionfeedback/mobile", { "content": content ,"contact" : contact }, function (data) {
//    }, "json").error(
//    );
//};
var reg = {};
reg.vCodeObj = null;
reg.curTime = 59;
reg.vCode = function () {
    /// <summary>点击后有1分钟倒计时</summary>
    $("input.btnVCode").click(function () {
        if (!document.getElementById("phone") || $.trim($("#phone").val()) == "") {
            messageBox.show("请先填写手机号码！");
            return false;
        }
        if (!document.getElementById("txtCaptcha") || $.trim($("#txtCaptcha").val()) == "") {
            messageBox.show("请先填写图片验证码");
            return false;
        }
        var mobStr = $.trim($("#phone").val());
        if (!isMob(mobStr)) {
            messageBox.show("所填的手机号码格式不正确！");
            return false;
        }
        reg.vCodeObj = $(this);
        reg_ajax.verification($.trim($("#phone").val()), $.trim($("#txtCaptcha").val()), function () {
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
        });
    });
};
//FIXME: fixme
reg.vCode1 = function () {
    /// <summary>点击后有1分钟倒计时</summary>
    $("input.btnVCode1").click(function () {
        if (!document.getElementById("txtMob") || $.trim($("#txtMob").val()) == "") {
            messageBox.show("请先填写手机号码！");
            return false;
        }
        var mobStr = $.trim($("#txtMob").val());
        if (!isMob(mobStr)) {
            messageBox.show("所填的手机号码格式不正确！");
            return false;
        }
        reg.vCodeObj = $(this);
        reg_ajax.verification1($.trim($("#txtMob").val()), $.trim($("#txtCaptcha").val()), function () {
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
        });
    });
};
reg.price = function (count) {
    var cobj = $("#orderprice label");
    if (!cobj.attr("p")) {
        cobj.attr("p", parseFloat(cobj.text()));
    }
    var price = parseFloat(cobj.attr("p"));
    cobj.text(price * count);

};
reg.addNumber = function () {
    /// <summary>自定义的加数字的控件</summary>
    $("a.jia").click(function () {
        var orderCount = parseInt($("#orderCount label").text());
        $("#hd_count").val(orderCount + 1);
        $("#orderCount label").text(orderCount + 1);
        reg.price(orderCount + 1);
    });
    $("a.jian").click(function () {
        var orderCount = parseInt($("#orderCount label").text());
        if (orderCount > 1) {
            $("#hd_count").val(orderCount - 1);
            $("#orderCount label").text(orderCount - 1);
            reg.price(orderCount - 1);
        }

    });
};
reg.init = function () {
    reg.vCode();
    reg.vCode1();
    reg.addNumber();
    $("#loadmore").click(function () {
        reg_ajax.getprojects($(this).attr("size"), $(this));
    });
};

var data = {};
data.setAddress = function () {
    $("#btnSaveAddr,#btnSaveDefAddr").click(function () {
        var test_obj = null;
        if (document.getElementById("isMob")) {
            test_obj = $(this).parents("#win2");
        }
        else {
            test_obj = $(this).parents("form");
        }
        if (test_obj.test()) {
            var str_address = "";
            if (document.getElementById("sls_pro")) {
                str_address = $("#sls_pro option:selected").text()
                + $("#sls_city option:selected").text()
                + $("#sls_dist option:selected").text() + ", " + $("#txtAddr").val();
            }
            else {
                str_address = $("#txtAddr").val();
            }
            if (document.getElementById("win2")) {
                reg_ajax.addOrderAddress(clickOpen_obj.attr("addrid"), "true", $.trim($("#txtName").val()), $.trim($("#txtMob").val()), $.trim($("#txtPost").val()), str_address, $(this));
            } else {
                reg_ajax.setAddress("true", $.trim($("#txtName").val()), $.trim($("#txtMob").val()), $.trim($("#txtPost").val()), str_address, $.trim($("#return").val()), $(this));
            }
        }
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
data.init = function () {
    data.pro();
    data.setAddress();
    data.setDefAddress();
};
function words() {
    $(".pre").each(function () {
        var newWord = "<p>" + $.trim($(this).text()).replace(/\s{3,}/g, "</p><p>") + "</p>";
        $(this).html(newWord);
    });
}
var home = {
    cur: 0,
    isScroll: true,
    scroll: function (isUp) {
        /// <summary>页面随手势滚动</summary>
        if (home.isScroll) {

            if (isUp) {
                home.cur++;
            }
            else {
                home.cur--;
            }
            if (home.cur < 1) {
                home.cur = 1;
                return false;
            }
            if (home.cur > $(".page").size()) {
                home.cur = $(".page").size();
                return false;
            }
            home.isScroll = false;
            $('body,html').animate({ scrollTop: $("#page" + home.cur).position().top }, "slow", function () {
                home.isScroll = true;
            });
        }
    },
    init: function () {
        if (document.getElementById("page1")) {
            $(".page").each(function (idx) {
                $(this).css({ "top": idx * 100 + "%", "display": "block" });
            });
            $(".page").touchwipe({
                wipeUp: function () {
                    /// <summary>手势从上往下滑</summary>
                    home.scroll(false);
                },
                wipeDown: function () {
                    /// <summary>手势从下往上滑</summary>
                    home.scroll(true);
                },
                min_move_x: 20,
                min_move_y: 20,
                preventDefaultEvents: true
            });
        }
    }
};
var clickOpen_obj;
$(function () {
    home.init();
    menu.init();
    reg.init();
    data.init();
    tab.show();
    if ($("#tip").size() == 0) {
        $("body").append('<div id="tip"><div id="vcenter"><div></div></div></div>');
    }
    $("#btnshare").click(function () {
        $("#sharelayer").fadeIn("normal");
    });
    $("#sharelayer a.btn").click(function () {
        $("#sharelayer").css("display", "none");
        $("#shareTip").css("display", "none");
    });
    $("#my_orders .loadmore").click(function () {
        reg_ajax.salons(parseInt($(this).attr("size")), "my", $(this));
    });
    $("#all_orders .loadmore").click(function () {
        reg_ajax.salons(parseInt($(this).attr("size")), "all", $(this));
    });
    $("#btnMessage").click(function () {
        if ($.trim($("#txtMessage").val()) == "") {
            messageBox.show("请先输入聊天内容！");
            return false;
        }
        reg_ajax.message($("#txtMessage").attr("profileid"), $("#txtMessage").val(), $(this));
    });
    $("#btn_salonMsg").click(function () {
        var str_chat = $.trim($("#txtMessage").val());
        if (str_chat == "") {
            messageBox.show("请先输入聊天内容！");
            return false;
        }
        reg_ajax.chat(str_chat, $(this));
    });
    if ($('.imgView').size() > 0 && $('.imgView img').size() > 1) {
        var slideObj = {};
        slideObj.play = {
            height: 150,
            active: true,
            auto: true,
            interval: 5000,
            swap: true
        }
        $('.imgView').slidesjs(slideObj);
    }
    else {
        $('.imgView').css("display", "block");
    }
    $("#yzm").click(function () {
        $(this).find("img").attr("src", $(this).find("img").attr("src"));
        return false;
    });
    if (document.getElementById("bookCover")) {
        $("#bookCover").append('<div id="picBg">&nbsp;</div>');
    }
    $("#share_wx").click(function () {
        $("#shareTip").css("display", "block");
        setTimeout(function () {
            $("#shareTip").css("display", "none");
        }, 3000);
    });
    $("#btn_wx").click(function () {
        $("#shareTip,#sharelayer").css("display", "block");
        setTimeout(function () {
            $("#sharelayer").css("display", "none");
        }, 3000);
    });
    $(".zsUserList .pagers a").click(function () {
        $(".zsUserList .pagers a").removeClass("btn");
        $(this).addClass("btn");
        var idx = $(this).index();
        if (idx == 0) {
            $(".zsUserList ul").css("display", "block");
        }
        else {
            idx = idx - 1;
            $(".zsUserList ul").not("#slnList").css("display", "none").eq(idx).css("display", "block");
        }
    });
    //支付金额提醒
    var isClickPlay = true;
    $("#payment").click(function () {
        if ($(".bigReg").test()) {
            var $obj = $(this);
            if (isClickPlay) {
                isClickPlay = false;
                setTimeout(function () {
                    isClickPlay = true;
                    $obj.css("background-color", "#fff");
                }, 10000);
                $obj.css("background-color", "#ccc");
                if (parseFloat($("#orderprice label").text()) >= 3000) {
                    layer.open({
                        content: $("#tip_font").html(),
                        btn: ['我知道了'],
                        title: '重要提示',
                        yes: function () {
                            layer.closeAll();
                            //点我知道了调用的方法
                            //$('#form')[0].submit();
                        }
                    });
                } else {
                    $('#form')[0].submit();
                }
            }
        }
    });
    /*修改地址点击修改*/
    $("#crt_addEmail").click(function () {
        /// <summary>点击弹出邮件添加弹层</summary>
        $("#win").fadeIn("normal");
    });
    $("#layer_close").click(function () {
        $("#win").hide();
    });
    $("#btnAddShowEmail").click(function () {
        if ($("#win").test()) {
            var str_address = $.trim($("#txtName").val()) + "," + $.trim($("#txtMobile").val()) + "," + $.trim($("#txtAddr").val()) + "," + $.trim($("#txtPost").val());
            $("#address").val(str_address);
            $("#crt_addEmail .oldVal").text(str_address);
            $("#win").hide();
        }
    });
    /*2015-09-21*/
    $("a.link_editAddr").click(function () {
        clickOpen_obj = $(this);
        $("#win").fadeIn("normal");
    });
    $("#btn_addMobAddr").click(function () {
        $("#win2").fadeIn("normal");
    });
    $("#layer_close2").click(function () {
        $("#win2").hide();
    });

    $("#btn_editMobAttr").click(function () {
        /// <summary>选择已有地址</summary>
        if ($("[name='addr']:checked").size() == 0) {
            messageBox.show("请先选择一个地址！");
            return false;
        }
        reg_ajax.editAddress(clickOpen_obj.attr("addrid"), $(this), $("[name='addr']:checked").next().text());
    });

});