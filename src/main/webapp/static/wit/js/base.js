var h5 = {
    page: {
        cur: 0,
        isScroll: true,
        scroll: function (isUp) {
            /// <summary>页面随手势滚动</summary>
            /// <param name="isUp" type="bool">是否是向上翻页</param>
            if (h5.page.isScroll) {

                if (isUp) {
                    h5.page.cur++;
                }
                else {
                    h5.page.cur--;
                }
                if (h5.page.cur < 0) {
                    h5.page.cur = 0;

                }
                if (h5.page.cur > $(".page").size()) {
                    h5.page.cur = $(".page").size();
                    return false;
                }
                h5.page.isScroll = false;

                var scrollHg = $("#page1").height() * h5.page.cur;
                $('body,html').animate({ scrollTop: scrollHg }, "slow", function () {
                    h5.page.isScroll = true;
                });
            }
        }
    },
    init: function () {
        /// <summary>初始化，绑定一些事件</summary>
        $("#btn_reg").click(function (e) {
            /// <summary>第一步，填写您的信息</summary>
            if ($("#reg").test()) {
                h5.page.scroll(true);
            }

            e.preventDefault();
        });
        $("#page2 .div_tip").click(function () {
            /// <summary>第二步，选择方案</summary>
            if ($(this).is(".cur")) {
                $("#page2 .div_tip").removeClass("cur");
            }
            else {
                $("#page2 .div_tip").removeClass("cur");

                $(this).addClass("cur");
            }
        });
        $("#btn_confirm").click(function (e) {
            /// <summary>第三步，确认信息</summary>
            if ($("#page2 .cur").size() > 0) {

                $("#regList li").each(function (idx) {
                    $(this).find("label").text($("#reg :text").eq(idx).val());
                });
                //姓名
                $("#hd_name").val($.trim($("#txt_name").val()));
                //手机
                $("#hd_mob").val($.trim($("#txt_mob").val()));
                //邮箱
                $("#hd_email").val($.trim($("#txt_email").val()));
                //职业
                $("#hd_job").val($.trim($("#txt_job").val()));
                //选择的方案
                $("#hd_sln").val($("#page2 .cur").attr("slnid"));
                $("#sln_confirm").html($("#page2 .cur").html());
                h5.page.scroll(true);
            }
            else {
                messageBox.show("请在两个方案中任选其一。");
            }
            e.preventDefault();
        });

        $("#link_play").click(function (e) {
            /// <summary>第四步，确认支付</summary>
            if ($("#hd_sln").val() != "") {
                //触发post提交
                $("#sbt_play").click();
            }
            else {
                messageBox.show("所填数据不完整，请补全后再提交，谢谢！");
            }
            e.preventDefault();
        });
        $("#link_answer").click(function (e) {
            /// <summary>第5步，填写提问</summary>
            if ($.trim($("#txtr_answer").val()) == "") {
                messageBox.show("提问内容不能为空！");
                return false;
            }
            if ($.trim($("#txtr_answer").val()).length < 10) {
                messageBox.show("提问内容不能少于10个字！");
                return false;
            }
            $("#sbt_answer").click();
            e.preventDefault();
        });
        $(".page").touchwipe({
            wipeUp: function () {
                /// <summary>手势从上往下滑</summary>
                h5.page.scroll(false);
            },
            wipeDown: function () {
                /// <summary>手势从下往上滑</summary>

                //                switch (parseInt(h5.page.cur)) {
                //                    case 2:
                //                        {
                //                            $("#btn_reg").click();
                //                        }
                //                        break;
                //                    case 3:
                //                        {
                //                            $("#btn_confirm").click();
                //                        }
                //                        break;
                //                }

            },
            min_move_x: 20,
            min_move_y: 20,
            preventDefaultEvents: true
        });
    }
};
$(function () {
    if (document.getElementById("page3")) {
        var pageHg = $("#page1").height();

        $(".page").each(function (idx) {
            $(this).css({ "height": pageHg + "px", "top": idx * pageHg + "px", "display": "block" }).attr("old", pageHg);
        });
    }
    h5.init();
    $(window).resize(function () {

        $('body,html').animate({ scrollTop: 0 }, 0);
    });
});