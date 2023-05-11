// 控件生成器（博客标签复选框）
function make_blog_mark_checkbox(data) {
    if (data['mark_id'] == 0) return null;
    return `
        <label class="blog-mark-item">
            <input type="checkbox" name="mark_checkbox" value=${data['mark_id']} style="vertical-align: middle" mark_name=${data['name']} />
            <span style="vertical-align: middle">${data['name']}</span>
        </label>
        `;
}

// 控件生成器（博客标签）
function make_blog_mark(data) {
    return `
        <span class="attr_bar_item blog_mark" mark_id=${data['mark_id']}>${data['name']}</span>
        `;
}

// 控件生成器（分类专栏复选框）
function make_column_checkbox(data) {
    return `
        <label class="column-item">
            <input type="checkbox" name="col_checkbox" value=${data['col_id']} style="vertical-align: middle" mark_name=${data['name']}/>
            <span style="vertical-align: middle">${data['name']}</span>
        </label>
        `;
}

// 搜索标签
function search_mark(key) {
    if (key === "") return;
    $.post('/searchMark', {
            search_key: key
        },
        function (res) {
            const marks = res['data']['marks'];
            $.push_back_controller($('.mark_search_result'), make_blog_mark_checkbox, marks);
        });
}

// 新建标签复选框
function create_mark_checkbox(creator_id, name) {
    $.post('/createMark',
        {
            creator_id: creator_id,
            mark_name: name
        },
        function (res) {
            const marks = [{mark_id: res['data']['mark_id'], name: name}];
            $.push_front_controller($('.mark_search_result'), make_blog_mark_checkbox, marks);
        });
}

//新建分类专栏
// 专栏名（String）、添加到哪个地方（JQ变量）、控件构造器
function create_col(name, target = null, make = null) {
    $.post('/createBlogColumn', {
        creator_id: $.cookie('id'),
        col_name: name
    }, function (res) {
        if (target != null && make != null) {
            const cols = [{col_id: res['data']['col_id'], name: name}];
            $.push_back_controller(target, make, cols);
        }
    })
}

//加载分类专栏复选框
function get_user_col_checkbox() {
    $.ajax({
        type: "POST",
        url: '/getUserColumn',
        async: false,
        success: function (res) {
            const cols = res['data']['columns'];
            $.push_back_controller($('.column-list'), make_column_checkbox, cols);
        }
    });
}

// 保存标签按钮
function save_mark_btn() {
    $('.add_mark_dropdown').toggleClass('open');
    const checked_mark = $('[name=mark_checkbox]:checked');
    let marks = [];
    for (let i = 0; i < checked_mark.length; i++) {
        marks.push({
            mark_id: $(checked_mark[i]).val(),
            name: $(checked_mark[i]).attr('mark_name')
        });
    }
    $('.mark_search_result').empty();
    $.push_front_controller($('.attr_bar'), make_blog_mark, marks);
    $('.blog_mark').click(function () {
        $(this).remove();
    });
}

//关闭分栏按钮
function close_col_btn() {
    $('.add_col_dropdown').toggleClass('open');
}

// 发布博客
function publish_blog(title, pre_text, md_code, is_original, marks_id = [], cols_id = []) {
    if (typeof title != 'string' || title.length < 5) {
        alert('标题长度不得小于5个字符');
        return;
    }
    $.post('/publishBlog',
        {
            title: title,
            pre_text: pre_text,
            md_code: md_code,
            is_original: is_original,
            marks_id: marks_id,
            cols_id: cols_id
        },
        function (res) {
            if (res['success'] == true) {
                alert('发布成功');
                window.location = res['data']['blog_url'];
            } else {
                alert(res['message']);
            }
        })
}

// 保存博客
function save_blog(blog_id, title, pre_text, md_code, is_original, marks_id = [], cols_id = []) {
    if (typeof title != 'string' || title.length < 5) {
        alert('标题长度不得小于5个字符');
        return;
    }
    $.post('/saveBlog',
        {
            blog_id: blog_id,
            title: title,
            pre_text: pre_text,
            md_code: md_code,
            is_original: is_original,
            marks_id: marks_id,
            cols_id: cols_id
        },
        function (res) {
            if (res['success'] == true) {
                alert(res['info']);
                window.location = res['data']['blog_url'];
            } else {
                alert(res['message']);
            }
        })
}