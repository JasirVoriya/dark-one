// 控件生成器（博客列表条目）
function make_blog_list_item(data) {
    if (data == null) return null;
    return `
        <li class="consoleHome-listData" blog_id=${data['blog_id']}>
            <div class="consoleHome-listData-left">
                <span class="consoleHome-listData-mark">博客</span>
                <div class="consoleHome-listData-desc">
                    <a href=${data['view_url']} target="_blank">
                        <p class="title" title=${data['title']}>${data['title']}</p>
                        <p title=${$.str_code(data['pre_text'])}>${$.str_code(data['pre_text'])}</p>
                    </a>
                </div>
                <div style="min-width: 60px">
                    <a class="edit-icon" href=${data['edit_url']} target="_blank">
                        <span class="glyphicon glyphicon-edit"></span><span></span>编辑
                    </a>
                </div>
            </div>

            <div>
                <ul class="consoleHome-listData-num">
                    <li>
                        <p class="listNum">${data['comment_count']}</p>
                        <p>评论数</p>
                    </li>
                    <li><p class="listNum">${data['like_count']}</p>
                        <p>点赞数</p></li>
                    <li><p class="listNum">${data['collection_count']}</p>
                        <p>收藏数</p></li>
                </ul>
            </div>
        </li>
        `;
}

// 控件生成器（管理博客条目，可删除博客）
function make_console_blog_list_item(data) {
    if (data == null) return null;
    return `
        <li class="consoleHome-listData" blog_id=${data['blog_id']}>
            <div class="consoleHome-listData-left">
                <span class="consoleHome-listData-mark">博客</span>
                <div class="consoleHome-listData-desc">
                    <a href=${data['view_url']} target="_blank">
                        <p class="title" title=${data['title']}>${data['title']}</p>
                    </a>
                </div>
                <div style="min-width: 60px">
                    <a class="edit-icon" href=${data['edit_url']} target="_blank">
                        <span class="glyphicon glyphicon-edit"></span><span></span>编辑
                    </a>
                </div>
                <div style="min-width: 60px">
                    <span class="del-icon" blog_id=${data['blog_id']} blog_title=${data['title']}>
                        <span class="glyphicon glyphicon-edit"></span><span></span>删除
                    </span>
                </div>
            </div>
        </li>
        `;
}

// 控件生成器（博客专栏列表条目）
function make_col_list_item(data) {
    if (data == null) return null;
    return `
        <li class="consoleCol-listData" col_id=${data['col_id']} title=${data['name']}>
            <div class="consoleHome-listData-left">
                <span class="consoleHome-listData-mark">分栏</span>
                <div class="consoleCol-listData-desc">
                    <p class="col-title" col_id=${data['col_id']}>${data['name']}</p>
                </div>
            </div>
            <div>
                <div style="min-width: 60px">
                    <a class="col-edit-btn" col_id=${data['col_id']}>编辑</a>
                </div>
                <div style="min-width: 60px">
                    <a class="col-delete-btn" col_id=${data['col_id']}>删除</a>
                </div>
            </div>
        </li>
        `;
}

// 解析地址栏的参数，激活对应的导航栏
function action_tab(index) {
    $('.container-left li')[index].className = "active";
    $('.tab-pane')[index].className += " active";
}
const blog_size = 10;
// 为分栏按钮绑定点击事件
function col_btn_click() {
    // 点击分栏标题
    $('.col-title').click(function () {
        let col_id = $(this).attr('col_id');
        let name = $(this).text();
        $.get('/getColBlogNum', {col_id: col_id}, function (res) {
            let data = res['data'];
            $.paginator('#colum-page-wrap', Math.ceil(data['blog_num'] / blog_size), 5, 1, function (num) {
                $.get('/getColBlogList', {col_id: col_id,size: blog_size, page: num}, function (res) {
                    let data = res['data']['data'];
                    let view_url = res['data']['view_url'];
                    let edit_url = res['data']['edit_url'];
                    if (data != null && data.length > 0) {
                        for (let i = 0; i < data.length; i++) {
                            data[i]['view_url'] = view_url + '?blog_id=' + data[i]['blog_id'];
                            data[i]['edit_url'] = edit_url + '?blog_id=' + data[i]['blog_id'];
                        }
                    }
                    $('.consoleColBlog-ul').empty();
                    $('#blog-list-title').text(name);
                    $.push_back_controller($('.consoleColBlog-ul'), make_console_blog_list_item, data);
                    blog_delete_btn_click();
                });
            });

        });

    });
    // 删除按钮
    $('.col-delete-btn').click(function () {
        let col_id = $(this).attr('col_id');
        let $item = $(`.consoleCol-listData[col_id=${col_id}]`);
        let $title = $item.find('.title');
        let title = $title.text();
        $.showConfirm(
            '确定',
            '确定删除分栏”' + title + '”（博客会移至默认分组）？',
            function () {
                $.get('/deleteCol', {col_id: col_id}, function (res) {
                    if (res['success']) {
                        $(`.consoleHome-listData[col_id=${col_id}]`).remove();
                        $.showSuccessTimeout(res['data']['info'], null);
                    } else {
                        $.showErr(res['message'], null);
                    }
                });
            }, null);
    });
    // 编辑按钮
    $('.col-edit-btn').click(function () {
        let col_id = $(this).attr('col_id');
        let $item = $(`.consoleCol-listData[col_id=${col_id}]`);
        let $title = $item.find('.title');
        let title = $title.text();
        $.showConfirm(
            '编辑分栏',
            $(`<input id="new-col-name" class="form-control" maxlength=30 type="text" placeholder="输入分栏名" value=${title}>`),
            function () {
                let new_name = $('#new-col-name').val();
                if (new_name.length < 5) {
                    $.showErr('分栏名太短', null);
                    return;
                }
                $.get('/editCol', {new_name: new_name, col_id: col_id}, function (res) {
                    if (res['success']) {
                        $title.text(new_name);
                        $.showSuccessTimeout('修改成功', null);
                    } else {
                        $.showErr(res['message'], null);
                    }
                });
            }, null);
    });
}

// 为删除博客按钮绑定事件
function blog_delete_btn_click() {
    $('.del-icon').click(function () {
        let blog_id = $(this).attr('blog_id');
        let blog_title = $(this).attr('blog_title');
        $.showConfirm('删除博客', `确定删除博客“${blog_title}”（该操作无法挽回）？`, function () {
            $.get('/deleteBlog', {blog_id: blog_id}, function (res) {
                if (res['success']) {
                    $(`.consoleHome-listData[blog_id=${blog_id}]`).remove();
                    $.showSuccessTimeout('删除成功');
                } else {
                    $.showErr(res['message']);
                }
            })
        }, null);
    });
}

$(function () {
    // 激活导航栏
    action_tab($.get_url_param('index'));
    //如果token正确
    if (verify()) {
        $('.avatar').attr('src', '/getAvatar?id=' + $.cookie('id'));
        // 显示头像
        $('#user').attr('style', '');
        // 获取博客信息
        $.get('/getUserBlogNum',function (res) {
            let data = res['data'];
            $.paginator('#content-page-wrap', Math.ceil(data['blog_num'] / blog_size), 5, 1, function (num) {
                $.get('/getUserBlogInfo', {size: blog_size, page: num}, function (res) {
                    let view_url = res['data']['view_url'];
                    let edit_url = res['data']['edit_url'];
                    let data = res['data']['data'];
                    if (data != null && data.length > 0) {
                        let sum_comment_count = 0, sum_like_count = 0, sum_collection_count = 0;
                        for (let i = 0; i < data.length; i++) {
                            data[i]['view_url'] = view_url + '?blog_id=' + data[i]['blog_id'];
                            data[i]['edit_url'] = edit_url + '?blog_id=' + data[i]['blog_id'];
                            sum_comment_count += data[i]['comment_count'];
                            sum_like_count += data[i]['like_count'];
                            sum_collection_count += data[i]['collection_count'];
                        }
                        let today_data = $('.today_data');
                        today_data[0].innerHTML = sum_comment_count;
                        today_data[1].innerHTML = sum_like_count;
                        today_data[2].innerHTML = sum_collection_count;
                        $('.consoleHome-ul').empty();
                        $('.consoleContent-ul').empty();
                        $.push_back_controller($('.consoleHome-ul'), make_blog_list_item, [data[0], data[1], data[2]]);
                        $.push_back_controller($('.consoleContent-ul'), make_blog_list_item, data);
                    }
                });
                console.log('当前第' + num + '页');
            });
        });
        //获取用户分栏
        $.get('/getUserColumn', function (res) {
            let columns = res['data']['columns'];
            $.push_back_controller($('.consoleColumn-ul'), make_col_list_item, columns);
            col_btn_click();
        });
        // 新建用户分栏
        $('#new-col-btn').click(function () {
            let col_name = $('#new-col-title-input').val();
            if (col_name == null || col_name.length < 5) {
                alert('标题太短');
                return;
            }
            $.get('/createBlogColumn', {col_name: col_name}, function (res) {
                if (res['data']['col_id'] == 0) return;
                let columns = [{col_id: res['col_id'], name: col_name}];
                $.push_back_controller($('.consoleColumn-ul'), make_col_list_item, columns);
                col_btn_click();
            })
        })
    }
});