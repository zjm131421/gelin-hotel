define(function (require,exports,module) {
    //			layui扩展模块的两种加载方式-示例
    //		    layui.extend({
    //			  admin: '{/}../../static/js/admin' // {/}的意思即代表采用自有路径，即不跟随 base 路径
    //			});
    //			//使用拓展模块
    //			layui.use('admin', function(){
    //			  var admin = layui.admin;
    //			});
    layui.config({
        base: '/static/js/weadmin/'
        ,version: '101100'
    }).use('admin');
    layui.use(['jquery','admin'], function(){
        var $ = layui.jquery;
        $(function(){
            var login = JSON.parse(localStorage.getItem("login"));
            if(login){
                if(login=0){
                    //window.location.href='./login.html';
                    return false;
                }else{
                    return false;
                }
            }else{
                //window.location.href='./login.html';
                return false;
            }
        });
    });
})
