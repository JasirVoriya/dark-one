// 控件生成器（评论列表条目）
function make_comment_list_item(data) {
    if (data == null) return null;
    return `
        <div class="comment-list-item">
            <div class="comment-left">
                <img src="/getAvatar?id=${data['user_id']}" class="comment-avatar">
                <div class="comment-content">
                    <p class="comment-nickname">${data['nickname']}</p>
                    <p class="comment-text">${data['text']}</p>
                </div>
            </div>
            <div class="comment-right">
                <p class="comment-time">${data['time']}</p>
            </div>
        </div>
        `;
}
