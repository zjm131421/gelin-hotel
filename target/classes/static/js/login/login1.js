layui.use(['layer', 'form', 'jquery'], function() {
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form;
    
    form.on('submit(login)', function () {
        var index = layer.load();
        $.ajax({
            type: 'get',
            url: nginx_url + '/login',
            data: {
                'username': $('#username').val(),
                'password': $('#password').val()
            },
            dataType: 'json',
            async: false,
            success: function (result) {
                console.log(result);
                if (result.STATUS === 'SUCCESS') {
                    $.cookie("loginId", result.USER.loginId);
                    setTimeout(function() {
                        layer.close(index);
                        layer.msg('登录成功', {
                            time: 800,
                            icon: 1
                        }, function () {
                            window.location.href = 'index.html';
                        });
                    }, 800);
                }
            }
        });
        return false;
    });
});
