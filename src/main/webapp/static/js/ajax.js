
var $btnObj = null;
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
    }, error: function (data) {
        /// <summary>错误处理</summary>
        if (!$btnObj.attr("notip")) {
            $btnObj.text($btnObj.attr("old"));
        }
        if (data.code && data.result) {
            alert(data.result);
            return false;
        }
        alert("有可能是网络阻塞或系统异常，请稍后重试！");
    }, isuc: function (data) {
        /// <summary>判断是否请求成功</summary>
        if (!$btnObj.attr("notip")) {
            $btnObj.text($btnObj.attr("old"));
        }
        if (data.code == "ok") {
            if (!$btnObj.attr("isclose")) {
                $(".win").fadeOut("fast");
                $("body").css("overflow-y", "auto");
            }
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
reg_ajax.imgServer = "http://7xizu1.com1.z0.glb.clouddn.com/@";
var server = server || "/";
reg_ajax.verification = function (phone, captcha, success, failure) {
    /// <summary>手机验证码</summary>
    $.post(server + "phone/verification", { "phone": phone, "captcha": captcha }, function (data) {
        if (data.code == "ok") {
            success();
        } else {
            if (data.result) {
                failure(data.result);
            }
        }
    }, "json");
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
            if (data.result) {
                window.location = data.result;
            } else {
                alert("添加成功");
            }
        }
    }, "json").error(reg_ajax.error);
};

reg_ajax.address = function (address, postCode, recipient, phone, btnObj, orderId) {
    /// <summary>8_1添加地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "profile/orders/" + orderId + "/address", { "address": address, "postCode": postCode, "recipient": recipient, "phone": phone}, function (data) {
        if (reg_ajax.isuc(data)) {
            if (data.result) {
                if (editAddrObj && editAddrObj.size()>0) {
                    editAddrObj.text(address);
                }
            } else {
                alert("添加成功");
            }
            window.location.reload();
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
            ui.pf();
        }

    }, "json").error(reg_ajax.error);
};
reg_ajax.setDefAddress = function (addressId, btnObj) {
    /// <summary>设置默认地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/addresses/" + addressId, { "_method_": "PUT" }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("设置成功！");
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.editAddress = function (orderId, btnObj, address) {
    /// <summary>8-1修改收件地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "profile/orders/" + orderId + "/address", { "_method_": "PUT", "address": address }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("修改成功！");
            if (editAddrObj && editAddrObj.size()>0) {
                editAddrObj.text(address);
            }
            window.location.reload();
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.bid = function (projectId, price, btnObj) {
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "projects/" + projectId + "/bid", { "price": price }, function (data) {
        if (reg_ajax.isuc(data)) {
            $(".price table tr").each(function () {
                var cobj = $(this).find("td:eq(0)");
                if ($.trim(cobj.text()) == $.trim(data.result.name)) {
                    cobj.parents("tr").remove();
                }
            });
            $(".price table").append('<tr><td>' + data.result.name + '</td><td>' + data.result.price + '/元</td><td class="result">&nbsp;</td></tr>')
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.salons = function (size, type, htmlControl, btnObj) {
    /// <summary>沙龙列表页</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("加载中…")) {
        return false;
    }
    var ajaxUrl = server + "salons";
    if (type == "all") {
        ajaxUrl = server + "salons/all";
    }
    var page = reg_ajax.getPage();
    $.get(ajaxUrl, { "page": page, "size": size }, function (data) {
        if (reg_ajax.isuc(data)) {
            var datas = data.result.content;
            reg_ajax.setPage(data, page);
            var ht = '';
            for (var d in datas) {
                d = datas[d];
                var chatCount = d.chat;
                if (parseInt(d.chat) >= 1000) {
                    chatCount = "1000+";
                }
                ht += '<div class="salong">\
                    <a href="' + d.salonId + '"><div><img src="' + d.image + '" /></div>\
                        <h3>' + d.name + '</h3>\
                        <ul>\
                            <li>' + d.members + '人参与</li><li class="line">|</li>\
                            <li>' + d.history + '个历史话题</li><li class="line">|</li>\
                            <li>' + chatCount + '条留言</li>\
                        </ul>\
                    </a>\
                    \
                </div>';

            }
            htmlControl.html(ht);
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
            var pobj = btnObj.parents("#bottomFlx");
            var ht = '<li class="my"><img src="' + pobj.find("img").attr("src") + '">\
            <div class="msgC"><p>' + chat + '</p>\
            <div class="msgName">我，<label>刚刚</label></div> </div></li>\
            \
            ';
            pobj.find("textarea").val("");
            $(".slDetails .msgList").prepend(ht);
        }
    }, "json").error(reg_ajax.error);
};

reg_ajax.uploadImg = function (files, imgControl) {
    /// <summary>上传图片</summary>
    if (!files) {
        return;
    }
    $.get("/storage/image/uptoken", function (data) {
        var formData = new FormData();
        formData.append("token", data.uptoken);
        formData.append("file", files);
        $.ajax({
            type: 'POST',
            url: 'http://upload.qiniu.com',
            data: formData,
            contentType: false,
            processData: false,
            success: function (data) {
                imgControl.find("img,.uploading,.imgClose").remove().end().append('<img src="' + reg_ajax.imgServer + data.result + '" /><div class="imgClose" title="删除图片">&nbsp;</div>');
                if (imgControl.attr("id")) {
                    var idStr = imgControl.attr("id");
                    if (idStr == "set_license") {
                        setting.license(data.result, imgControl);
                    }
                    else if (idStr == "upload_license") {
                        if (!document.getElementById("license")) {
                            imgControl.parents("form").append('<input type="hidden" id="license" name="license" value="' + data.result + '" />')
                        }
                        else {
                            $("#license").val(data.result);
                        }

                    }
                    else if (idStr == "id_front") {
                        setting.IDfront(data.result, imgControl);
                    }
                    else if (idStr == "id_back") {
                        setting.IDback(data.result, imgControl);
                    }
                    else if (idStr == "btn_avatar") {
                        setting.avatar(data.result, imgControl);
                    }
                }
                $(".imgClose").click(function () {
                    $(this).parents(".btnUpload").find("img").remove();
                    $(this).remove();
                });
            }
        });
    }, "json").error(function () {
        alert("获取token失败，请稍后重试！");
    });
};
reg_ajax.statusAjax = function (paymentid) {
    /// <summary>支付状态轮询检查</summary>
    $.get(server + "payments/" + paymentid + "/status", function (data) {
        if (data.code == "ok" && data.result.paid.toString() == "true") {
            clearInterval(stopAjax);
            window.location.href = data.result.callback;

        }
    }, "json").error(function () {
        clearInterval(stopAjax);
    });
};
var tmpl = {
    orders: function (data) {
        /// <summary>参与的项目html模板</summary>
        var smFont = "";
        var isfailure = data.state == "FAILURE";
        var isfunding = data.state == "FUNDING";
        if (isfunding) {
            smFont = reg_ajax.state[data.state] + "&nbsp;" + data.days + "天";
        }
        else {
            smFont = reg_ajax.state[data.state];
        }
        var ht = '<div class="order">';
        if (isfailure) {
            ht = '<div class="order fail">';
        }
        ht += '<div class="orderMsg"><label>' + data.createTime + '</label> \
                    <label>订单号：' + data.orderId + '</label>\
                    </div>\
                    <table class="table" cellpadding="0" cellspacing="0">\
                        <tbody>\
                            <td class="giftBook">\
                                <img src="' + data.cover + '" class="shadow">\
                                <a href=""></a>\
                            </td>\
                            <td width="24%">';
        if (data.rewards) {
            ht += '<ul class="giftList">';
            if (data.rewards.BOOK) {
                ht += '<li>' + data.rewards.BOOK + '</li>';
            }
            if (data.rewards.VIP) {
                ht += '<li>' + data.rewards.VIP + '</li>';
            }
            if (data.rewards.SIGNATURE) {
                ht += '<li>' + data.rewards.SIGNATURE + '</li>';
            }
            if (data.rewards.OTHER) {
                ht += '<li>' + data.rewards.OTHER + '</li>';
            }
            ht += '</ul>';
        }
        ht += '</td><td></td>\
                            <td width="120px">' + data.count + '份，￥5' + data.cost + '</td>\
                            <td width="120px">' + smFont + '</td>\
                        </tr>\
                    </tbody></table>\
                </div>';
        return ht;
    },
    myprojects: function (data) {
        /// <summary>8_1我发起的项目html模板</summary>
        var ht = '<div class="order">';
        var isfailure = data.state == "FAILURE";
        var isfunding = data.state == "FUNDING";
        if (isfailure) {
            ht = '<div class="order fail">';
        }
        var priceStr = (data.currentBalance ? "￥" + data.currentBalance : "-") + "/" + (data.goal ? "￥" + data.goal : "-");
        ht += '<div class="orderMsg">\
                        <label>' + data.createTime + '</label>\
                        <label>项目编号：' + data.projectId + '</label>';
        if (data.feedback && $.trim(data.feedback) != "") {
            ht += '<a class="remark">赞赏审核意见反馈<p>' + data.feedback + '</p></a>';
        }
        ht += '</div>\
                    <table class="table" cellpadding="0" cellspacing="0">\
                        <tbody><tr>\
                            <td class="giftBook">\
                                <img src="' + data.cover + '" class="shadow">\
                            </td>\
                            <td width="120">剩余' + data.days + '天</td>\
                            <td>筹款&nbsp;' + priceStr + '</td>\
                            <td>最低出版价￥' + data.feedbackGoal + '</td><td width="120px">';
        if (isfunding) {
            ht += '<a class="btnAudit">审核通过</a> <a>设置回报</a>';
        }
        else {
            ht += reg_ajax.state[data.state];
        }
        ht += '</td></tr></tbody></table></div>';
        return ht;
    },
    published: function (data) {
        /// <summary>8_3出版</summary>
        var ht = '<div class="order">';
        var isfailure = data.state == "FAILURE";
        var isfunding = data.state == "FUNDING";
        if (isfailure) {
            ht = '<div class="order fail">';
        }
        var priceStr = (data.currentBalance ? "￥" + data.currentBalance : "-") + "/" + (data.goal ? "￥" + data.goal : "-");

        ht += '<div class="orderMsg"><label>' + data.createTime + '</label><label>项目编号：' + data.projectId + '</label></div>';
        ht += '<table class="table" cellpadding="0" cellspacing="0"><tr><td class="giftBook"><img src="' + data.cover + '" class="shadow" /></td>';
        ht += '<td>剩余' + data.days + '天</td><td>' + priceStr + '</td><td width="120px">' + reg_ajax.state[data.state] + '</td></tr></table></div>';

        return ht;
    },
    getprojects: function (data) {
        /// <summary>项目列表页</summary>
        var ht = '<div class="author">';
        console.log(data);
        var sucNum = Math.floor(parseFloat(data.currentMoney) / parseFloat(data.goal) * 100);
        ht += '<div class="left"><div class="zsAuthor">';
        ht += '<a href="' + server + 'profile/' + data.uid + '"><img src="' + data.avatar + '">';
        ht += '<label>' + data.authorName + '</label></a></div>';
        ht += '<ul class="bookState">';
        if (sucNum >= 100) {
            ht += '<li class="suc">';
        }
        else {
            ht += '<li>';
        }
        ht += '<div class="progress">' + sucNum + '%</div>';
        ht += '<div>已筹</div></li>';
        if (data.passedDays >= data.totalDays) {
            ht += '<li class="suc">';
        }
        else {
            ht += '<li>';
        }
        ht += '<div class="progress">' + data.passedDays + ' / ' + data.totalDays + '</div>';
        ht += '<div>进行天数</div></li>';
        if (data.publisherLocked) {
            ht += '<li class="suc"><div class="progress">已支持</div>';
        }
        else {
            ht += '<li><div class="progress">/</div>';
        }
        ht += '<div>出版支持</div></li></ul></div>';
        ht += '<div class="center"><h3 class="bookName"><a href="' + server + 'projects/' + data.projectId + '">' + data.bookName + '</a></h3>';
        ht += '<ul class="bookTagList">';
        if (data.types) {
            for (var i = 0; i < data.types.length; i++) {
                ht += ' <li><a class="tag btn">' + data.types[i] + '</a></li>';
            }
        }
        ht += '</ul><p>' + data.description + '</p></div>';
        ht += '<div class="rightBook"> <a href="' + server + 'projects/' + data.projectId + '"><img src="' + data.cover + '"></a></div> </div>';
        return ht;
    }
};
reg_ajax.orders = function (size, btnObj) {
    /// <summary>8_1参与的项目</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("加载中…")) {
        return false;
    }
    var page = reg_ajax.getPage();
    $.get(server + "profile/orders", { "page": page, "size": size }, function (data) {
        if (reg_ajax.isuc(data)) {
            console.log(data);
            reg_ajax.setPage(data.result, page);
            var datas = data.result.content;
            var ht = '';
            for (var d in datas) {
                ht += tmpl.orders(datas[d]);
            }
            $("#item_orders .order").remove();
            $("#item_orders").prepend(ht);
            ui.pf();
        }

    }, "json").error(reg_ajax.error);
};
reg_ajax.myprojects = function (size, btnObj) {
    /// <summary>8_2发起的项目</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("加载中…")) {
        return false;
    }
    var page = reg_ajax.getPage();
    $.get(server + "profile/projects", { "page": page, "size": size }, function (data) {
        if (reg_ajax.isuc(data)) {
            console.log(data);
            reg_ajax.setPage(data.result, page);
            var datas = data.result.content;
            var ht = '';
            for (var d in datas) {
                ht += tmpl.myprojects(datas[d]);
            }
            $("#item_projects .order").remove();
            $("#item_projects").prepend(ht);
            ui.pf();
        }

    }, "json").error(reg_ajax.error);
};

reg_ajax.item_published = function (size, btnObj) {
    /// <summary>8_3出版</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("加载中…")) {
        return false;
    }
    var page = reg_ajax.getPage();
    $.get(server + "profile/projects", { "page": page, "size": size }, function (data) {
        if (reg_ajax.isuc(data)) {
            reg_ajax.setPage(data.result, page);
            var datas = data.result.content;
            var ht = '';
            for (var d in datas) {
                ht += tmpl.published(datas[d]);
            }
            $("#item_published .order").remove();
            $("#item_published").prepend(ht);
            ui.pf();
        }

    }, "json").error(reg_ajax.error);
};
reg_ajax.read = function (idsObj, btnObj) {
    /// <summary>修改阅读状态</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    var ids = [];
    idsObj.each(function () {
        ids.push($(this).attr("id"));
    });
    $.post(server + "notifications/read", { "ids": ids }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("标记成功！");
            idsObj.each(function () {
                $(this).parents("tr").addClass("suc").find("input:checkbox").remove();
            });
        }
    }, "json").error(reg_ajax.error);
};

reg_ajax.addprojects = function (userData, btnObj) {
    /// <summary>新增项目</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    var isEdit = $.trim(btnObj.attr("projectid")) != "";
    var ajaxUrl = server + "projects";
    if (isEdit) {
        ajaxUrl += "/" + $.trim(btnObj.attr("projectid"));
        userData._method_ = "PUT";
    }
    $.post(ajaxUrl, userData, function (data) {
        if (reg_ajax.isuc(data)) {
            if (isEdit) {
                alert("修改成功！");
            }
            else {
                layer2.open("#page5_7");
            }
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.previewprojects = function (userData, btnObj) {
    /// <summary>新增项目</summary>
    $btnObj = btnObj;
    var ajaxUrl = server + "projects/preview";
    submitForm(ajaxUrl, userData);
};
function submitForm(action, params) {
    var form = $("<form></form>");
    form.attr('action', action);
    form.attr('method', 'post');
    form.attr('target', '_blank');
    for(name in params){
        var input1 = $("<input type='hidden' name='"+ name +"' />");
        input1.attr('value', params[name]);
        form.append(input1);
    }
    form.appendTo("body");
    form.css('display', 'none');
    form.submit();
};
reg_ajax.draft = function (projectid, btnObj) {
    /// <summary>6.1.2编辑信息</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    var imgs = [];
    var ht = '';

    $("#editPicView .btnUpload img").each(function () {
        imgs.push($(this).attr("src").replace(reg_ajax.imgServer, ""));
        ht += '<img src="' + $(this).attr("src") + '" />';
    });
    $.post(server + "projects/" + projectid + "/draft", { "images": imgs, "draft": $("#editText").val(), "_method_": "PUT" }, function (data) {
        if (reg_ajax.isuc(data)) {
            $(".bookTextView .update").html($("#editText").val());
            ht += '<div class="pager_pic">';
            for (var i = 0; i < imgs.length; i++) {
                if (i == 0) {
                    ht += '<a class="cur">&nbsp;</a>';
                }
                else {
                    ht += '<a>&nbsp;</a>';

                }
            }
            ht += '</div>';
            $(".picView").html(ht);
            reg.editContent(false);
            alert("保存成功！");
            window.location.reload();
        }
    }, "json").error(reg_ajax.error);
    btnObj.remove();
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
        }
    }, "json").error(reg_ajax.error);
};
var setting = {};
setting.companyPhone = function (code, phone, btnObj) {
    /// <summary>7.2 联系人手机号</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/company/phone", { "code": code, "phone": phone }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("设置成功！");
        }
    }, "json").error(reg_ajax.error);
};
setting.publisherApply = function (btnObj) {
    /// <summary>申请成为出版社</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/publisher/apply", function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};
setting.name = function (name, btnObj) {
    /// <summary>7.1 里的笔名 7.2里的联系人名字</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/name", { "name": name }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};
setting.mails = function (email, btnObj) {
    /// <summary>7.1 里的邮箱 7.2里的机构邮箱</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/mails/activation", { "email": email }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};

setting.password = function (oldPassword, password, btnObj) {
    /// <summary>7.1 里的密码 7.2里的密码</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/password", { "oldPassword": oldPassword, "password": password }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("修改成功！");
        }
    }, "json").error(reg_ajax.error);
};

setting.delAddr = function (addressId, btnObj) {
    /// <summary>删除地址</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/addresses/" + addressId, { "_method_": "DELETE" }, function (data) {

        if (reg_ajax.isuc(data)) {
            btnObj.parents("li").remove();
        }
    }, "json").error(reg_ajax.error);
};

setting.companyCode = function (code, btnObj) {
    /// <summary>7.2 里的组织机构代码</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/company/code", { "code": code }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};

setting.license = function (image, btnObj) {
    /// <summary>7.2 里的营业执照</summary>
    $btnObj = btnObj;

    $.post(server + "settings/company/license", { "image": image }, function (data) {
        console.log(data);
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};


setting.companyName = function (name, btnObj) {
    /// <summary>7.2 里的公司名</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/company/name", { "name": name }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};

setting.legalname = function (name, btnObj) {
    /// <summary>7.1 里的姓名（不是笔名）</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/personal/legalname", { "name": name }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};

setting.IDfront = function (image, btnObj) {
    /// <summary>7.1 里的身份证正面</summary>
    $btnObj = btnObj;
    $.post(server + "settings/personal/identity/front", { "image": image }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};

setting.identity = function (identity, btnObj) {
    $btnObj = btnObj;
    $.post(server + "settings/personal/identity", { "identity": identity }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};
setting.IDback = function (image, btnObj) {
    /// <summary>7.1 里的身份证背面</summary>
    $btnObj = btnObj;
    $.post(server + "settings/personal/identity/back", { "image": image }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};

setting.personalPhone = function (code, phone, btnObj) {
    /// <summary>7.1 里的手机号</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/personal/phone", { "code": code, "phone": phone }, function (data) {
        if (data.code == "ok") {
            alert("提交成功！");
        } else {
            if (data.result) {
                alert(data.result);
            } else {
                alert("绑定失败,请重试!");
            }
        }
        $btnObj.text($btnObj.attr("old"));
    }, "json").error(reg_ajax.error);
};
setting.avatar = function (avatar, btnObj) {
    $btnObj = btnObj;
    $.post(server + "settings/avatar", { "image": avatar }, function (data) {

        if (reg_ajax.isuc(data)) {
        }
    }, "json").error(reg_ajax.error);
};
setting.qq = function (qq, btnObj) {
    /// <summary>qq号</summary>
    $btnObj = btnObj;
    if (reg_ajax.btn("")) {
        return false;
    }
    $.post(server + "settings/personal/qq", { "qq": qq }, function (data) {
        if (reg_ajax.isuc(data)) {
            alert("提交成功！");
        }
    }, "json").error(reg_ajax.error);
};
reg_ajax.setComments = function (content, contact) {
    /// <summary>意见的提交</summary>
    $.post(server + "suggestionfeedback", { "content": content ,"contact" : contact }, function (data) {
    }, "json").error(
    );

};