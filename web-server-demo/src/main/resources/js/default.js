function verify() {
    //定义一个全局变量来接受$post的返回值
    var result = false;
    //用ajax的“同步方式”调用一般处理程序
    $.ajax({
        url: '/verify',
        async: false,//改为同步方式
        type: "POST",
        success: function (res) {
            //如果token解析成功
            if (res['data']['data'] != null)
                result = true;
        }
    });
    return result;
}

(function ($) {
    $.get_url_param = function (name) {
        const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        const r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
})(jQuery);
(function ($) {
    $.set_url_param = function (url, arg, arg_val) {
        const pattern = arg + '=([^&]*)';
        const replaceText = arg + '=' + arg_val;
        if (url.match(pattern)) {
            let tmp = '/(' + arg + '=)([^&]*)/gi';
            tmp = url.replace(eval(tmp), replaceText);
            return tmp;
        } else {
            if (url.match('[\?]')) {
                return url + '&' + replaceText;
            } else {
                return url + '?' + replaceText;
            }
        }
    }
})(jQuery);

// 用来添加控件的函数
// target是我们需要添加控件的元素（JQ变量），make是生成控件的函数，data是控件数据（JSON数组）
(function ($) {
    $.push_back_controller = function (target, make, data_array) {
        for (let i = 0; i < data_array.length; i++) {
            target.append(make(data_array[i]));
        }
    }
})(jQuery);
(function ($) {
    $.push_front_controller = function (target, make, data_array) {
        for (let i = 0; i < data_array.length; i++) {
            target.prepend(make(data_array[i]));
        }
    }
})(jQuery);
(function ($) {
    $.str_code = function (value) {
        value = value.replace(/&/g, "&amp;");
        value = value.replace(/</g, "&lt;");
        value = value.replace(/>/g, "&gt;");
        value = value.replace(/ /g, "&nbsp;");
        value = value.replace(/"/g, '&quot;');
        return value;
    }
})(jQuery);
(function ($) {
    $.str_decode = function (value) {
        value = value.replace(/&amp;/g, "&");
        value = value.replace(/&lt;/g, "<");
        value = value.replace(/&gt;/g, ">");
        value = value.replace(/&nbsp;/g, " ");
        value = value.replace(/&quot/g, "'");
        return value;
    }
})(jQuery);
$.showConfirm = function (title, message, func_ok, func_close) {
    BootstrapDialog.confirm({
        title: title,
        message: message,
        type: BootstrapDialog.TYPE_WARNING, // <-- Default value is
        // BootstrapDialog.TYPE_PRIMARY
        closable: true, // <-- Default value is false，点击对话框以外的页面内容可关闭
        draggable: true, // <-- Default value is false，可拖拽
        btnCancelLabel: '取消', // <-- Default value is 'Cancel',
        btnOKLabel: '确定', // <-- Default value is 'OK',
        btnOKClass: 'btn-warning', // <-- If you didn't specify it, dialog type
        size: BootstrapDialog.SIZE_SMALL,
        // 对话框关闭的时候执行方法
        onhide: func_close,
        callback: function (result) {
            // 点击确定按钮时，result为true
            if (result) {
                // 执行方法
                func_ok();
            }
        }
    });
};
//弹出错误提示的登录框
$.showErr = function (str, func) {
    // 调用show方法
    BootstrapDialog.show({
        type: BootstrapDialog.TYPE_DANGER,
        title: '错误 ',
        message: str,
        size: BootstrapDialog.SIZE_SMALL,//size为小，默认的对话框比较宽
        buttons: [{// 设置关闭按钮
            label: '关闭',
            action: function (dialogItself) {
                dialogItself.close();
            }
        }],
        // 对话框关闭时带入callback方法
        onhide: func
    });
};
$.showSuccessTimeout = function (str, func) {
    BootstrapDialog.show({
        type: BootstrapDialog.TYPE_SUCCESS,
        title: '成功 ',
        message: str,
        size: BootstrapDialog.SIZE_SMALL,
        buttons: [{
            label: '确定',
            action: function (dialogItself) {
                dialogItself.close();
            }
        }],
        // // 指定时间内可自动关闭
        // onshown : function(dialogRef) {
        //     setTimeout(function() {
        //         dialogRef.close();
        //     }, YUNM._set.timeout);
        // },
        onhide: func
    });
};

// 禁用按钮
function disabledButton(buttonId, text, second) {
    $("#" + buttonId).attr({
        "disabled": "disabled"
    }); //控制按钮为禁用
    const intervalObj = setInterval(function () {
        $("#" + buttonId).text(text + "(" + second + ")");
        if (second == 0) {
            $("#" + buttonId).text(text);
            $("#" + buttonId).removeAttr("disabled"); //将按钮可用
            /* 清除已设置的setInterval对象 */
            clearInterval(intervalObj);
        }
        second--;
    }, 1000);
}