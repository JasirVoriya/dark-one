// 格式化日期函数
function date_format(date) {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    return year + '-' + (month < 10 ? '0' + month : month) + '-' + day;
}

// 日期选择器函数
function date_picker(element) {
    let date = new Date(element.val());
    let selected = new Date(date);
    let head = $('.picker__header-label');
    let body = $('.date-table td');
    date.setDate(1);

    function update_head() {
        $(head[0]).text(date.getFullYear() + ' 年');
        $(head[1]).text(date.getMonth() + 1 + ' 月');
    }

    function update_body() {
        let day = new Date(date.getFullYear(), date.getMonth(), 1).getDay();
        let cnt = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
        let last_month_cnt = new Date(date.getFullYear(), date.getMonth(), 0).getDate() - day + 1;
        // 设置上一个月的日期
        for (let i = 0; i < day; i++) {
            $(body[i]).text(last_month_cnt + i);
            body[i].className = 'prev-month';
        }
        // 设置当前月的日期
        let copy = new Date(date);
        for (let i = 0; i < cnt; i++) {
            $(body[i + day]).text(i + 1);
            copy.setDate(i + 1);
            // 如果日期比当前日期还大，设置成不可用
            if (copy.getTime() > new Date().getTime()) {
                body[i + day].className = 'not-available';
            } else {
                // 如果是选定结果那天，就选中他
                if (selected.getTime() === copy.getTime())
                    body[i + day].className = 'available active';
                else body[i + day].className = 'available';
            }
        }
        // 设置下个月的日期
        for (let i = 0; day + cnt + i < body.size(); i++) {
            $(body[day + cnt + i]).text(i + 1);
            body[day + cnt + i].className = 'next-month';
        }
        //点击日期选择生日，并关闭日期选择器
        $('.date-table td.available').click(function () {
            $('.date-table td.active').removeClass('active');
            $(this).addClass('active');
            // 将选择的日期保存到生日
            date.setDate($(this).text());
            //更新元素信息
            element.val(date_format(date));
            // 隐藏日期选择器
            $('.date-picker').hide();
        });
        $('.date-table td').not('.available').off('click');
    }

    // 加载日期选择器的数据
    update_head();
    update_body();

//         年按钮
    $('#left-year').click(function () {
        date.setFullYear(date.getFullYear() - 1);
        update_head();
        update_body();
    });
    $('#right-year').click(function () {
        date.setFullYear(date.getFullYear() + 1);
        update_head();
        update_body();
    });
//         月按钮
    $('#left-month').click(function () {
        date.setMonth(date.getMonth() - 1);
        update_head();
        update_body();
    });
    $('#right-month').click(function () {
        date.setMonth(date.getMonth() + 1);
        update_head();
        update_body();
    });
}

$(function () {
    // 鼠标悬浮信息区域显示编辑按钮
    $('.base-info-content-show').mouseover(function () {
        $(this).children('.edit-icon').show();
        $(this).css('background', '#e2e2e2');
    }).mouseout(function () {
        $(this).children('.edit-icon').hide();
        $(this).css('background', '#ffffff');
    });
    // 点击编辑按钮进入编辑
    $('.edit-icon').click(function () {
        $('.base-info-content-show').hide();
        $('.base-info-content-edit').show();
    });
    // 点击生日框打开日期选择器
    $('.date-editor').click(function () {
        $('.date-picker').show();
        // 重置日期选择器的年月为生日
        date_picker($('[name=birthday]'));
    });
    // 提示框事件
    $('.dialog-btn').click(function () {
        $('.dialog-wrap-box').hide();
        $('.base-info-content-show').show();
        $('.base-info-content-edit').hide();
    });
    $('.dialog-title-icon').click(function () {
        $('.dialog-wrap-box').hide();
        $('.base-info-content-show').show();
        $('.base-info-content-edit').hide();
    });
    // 点击保存按钮更新用户资料
    $('.save-button').click(function () {
        $.post('/updateUserInfo',
            {
                'nickname': $('[name=nickname]').val(),
                'real_name': $('[name=real-name]').val(),
                'city': $('[name=city]').val(),
                'sex': $('[name=sex]:checked').val(),
                'self_introduction': $('[name=self-introduction]').val(),
                'birthday': $('[name=birthday]').val()
            }, function (res) {
                if (res['success'] == true)
                    $('.dialog-wrap-box').show();
            });
    });
});