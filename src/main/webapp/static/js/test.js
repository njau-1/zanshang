
var validate = {
    /*新增验证方式，在此处新增属性*/
    email: [/\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/, "所填邮箱格式错误"],
    number: [/^[1-9]\d*$/, "请输入大于0的整数"],
    address: [/^(.|\n)+$/, "请填写收件人地址"],
    chinese: [/^[\u4e00-\u9fa5]+$/, "只能输入汉字"],
    english: [/^[a-zA-Z]+$/, "只能输入字母"],
    zipCode: [/^[0-9]{6}$/, "所填邮政编码格式错误"],
    webSite: [/^[a-zA-z]+:\/\/[^\s]*$/, "所填网址格式不正确"],
    mobile: [/^[0-9]{11}$/, "所填手机格式不正确"],
    mobileOrEmail: [mobileOrEmail, "所填数据格式不正确！"],
    confirmPwd: [confirmPwd, "两次密码输入不一致"]
};
validate.obj = null, validate.required = '';
function mobileOrEmail(obj) {
    /// <summary>验证是否是手机或邮箱格式</summary>
    var str_val = $.trim(obj.val());
    return validate.email[0].test(str_val) || validate.mobile[0].test(str_val);
}
function confirmPwd(obj) {
    /// <summary>确认密码</summary>
    var str_val = $.trim(obj.val());
    return str_val == $.trim(obj.parents("li").prev().find(":password").val());
}

/*数据获取对象*/
var Data = {
    getClass: function () {
        /// <summary>遍历获取所有类名属性</summary>

        var className = validate.obj.attr("class").replace("test", "");
        if (className.indexOf("required") != -1 && !Data.isNull()) {
            //必填验证未通过
            return false;
        }
        var classList = className.split(/\s+/);
        for (var i = 0; i < classList.length; i++) {
            var classStr = classList[i];
            if (!classStr || classStr == "required" || !validate[classStr]) {
                continue;
            }
            if (!Data.test(classStr)) {
                //数据验证失败
                return false;
            }
        }
        return true;
    },
    isNull: function (className) {

        //必填验证
        if ($.trim(validate.obj.val()) == "") {
            var font = validate.obj.attr("empty");
            if (!font) {
                font = validate.obj.attr("placeholder");
                if (!font) {
                    font = "请先将数据填写完整！";
                }
                if (font != "请先将数据填写完整！") {
                    font += "不能为空！";
                }
            }
            messageBox.show(font);
            return false;
        }
        return true;
    },
    test: function (className) {
        /// <summary>根据传人的class名进行具体的验证逻辑</summary>
        /// <param name="className" type="String">类名称,用以判断验证方式</param>

        var userVal = $.trim(validate.obj.val());
        var suc = true;
        //如果是需要验证的类名
        var typeStr = Object.prototype.toString.call(validate[className][0]);
        if (typeStr === "[object RegExp]") {
            //是正则表达式
            suc = validate[className][0].test(userVal);

        }
        else if (typeStr === "[object Function]") {
            //是自定义验证函数
            suc = validate[className][0](validate.obj);
        }

        if (!suc) {
            //验证失败弹出用户自己设置的文字提醒
            if (validate[className][1]) {
                messageBox.show(validate[className][1].replace("$标签名$", validate.obj.prev().text()));
            }
        }
        return suc;
    }
};

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

/*后端验证逻辑处理对象*/
var CsValidate = {};
$.fn.test = function () {
    /// <summary>验证用户数据</summary>
    /// <returns type="bool" >数据合格返回true,反之失败<returns>
    var suc = true;
    this.find(".test").each(function () {
        //把当前验证对象存储到全局变量
        validate.obj = $(this);
        if (!Data.getClass()) {
            //有一条验证失败暂停each遍历
            validate.obj.focus();
            suc = false;
            return false;
        }
    });
    return suc;
};

$(function () {
    $(".btntest").click(function () {
        return $(this).parents("form").test();
    });
    if ($("#tip").size() == 0) {
        $("body").append('<div id="tip"><div id="vcenter"><div></div></div></div>');
    }
});

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