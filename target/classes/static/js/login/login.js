define(function (require,exports,module) {
    require("validation");//引入校验模块
    require("validationRule");
    // require("cookie");
    require("md5");
    require("Particleground");
    $(document).ready(function () {
        init();

        $(".login_btn").click(function () {
            var resultError = ["请输入用户名", "请输入密码"];
            var result = $("#loginForm").validationEngine("validate",{
                showPrompts: false,
                onValidationComplete: function (data) {
                    if (this.isError) {
                        var index = $(this.InvalidFields[0]).attr("data-index");
                        $(this.InvalidFields[0]).addClass("valerror");
                        $("#loginForm").find(".error_msg").html("<p>" + resultError[index] + "</p>");
                    } else {
                        return true;
                    }
                }
            });
            if (result) {
                var pwd = $("input[name='password']");
                var pstr = pwd.val();
                if (pstr.length != 32) {
                    pwd.val(md5(pstr));
                    console.log(pwd.val())
                }
            }
            return result;
        });
    })

    function init() {
        // 粒子背景特效
        $('body').particleground({
            dotColor: '#E8DFE8',
            lineColor: '#133b88'
        });
        $('input[name="password"]').focus(function () {
            $(this).attr('type', 'password');
        });
        $('input[type="text"]').focus(function () {
            $(this).prev().animate({'opacity': '1'}, 200);
        });
        $('input[type="text"],input[type="password"]').blur(function () {
            $(this).prev().animate({'opacity': '.5'}, 200);
        });
        $('input[name="username"], input[name="password"]').keyup(function () {
            var Len = $(this).val().length;
            if (!$(this).val() === '' && Len >= 5) {
                $(this).next().animate({
                    'opacity': '1',
                    'right': '30'
                }, 200);
            } else {
                $(this).next().animate({
                    'opacity': '0',
                    'right': '20'
                }, 200);
            }
        });

    }


})