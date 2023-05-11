(function ($) {
    let pages_idx = 1;
    $.paginator = function (selector, totalPages, visiblePages, currentPage, onPageChange) {
        let $selector = $(selector);
        for (let i = 0; i < $selector.length; i++) {
            $($selector[i]).html(`
            <div class="pager">
                <ul class="pages" id="pages-${pages_idx}">
                </ul>
            </div>
            `);
            $(`#pages-${pages_idx}`).jqPaginator({
                totalPages: totalPages,
                visiblePages: visiblePages,
                currentPage: currentPage,

                first: '<li class="page-item first"><button class="pagination-btn num-btn">第一页</button></li>',
                prev: '<li class="page-item prev"><button class="pagination-btn num-btn">上一页</button></li>',
                next: '<li class="page-item next"><button class="pagination-btn num-btn">下一页</button></li>',
                last: '<li class="page-item last"><button class="pagination-btn num-btn">最后一页</button></li>',
                page: '<li class="page-item"><button class="pagination-btn num-btn">{{page}}</button></li>',
                onPageChange: onPageChange
            });
            console.log(pages_idx);
            pages_idx++;
        }
    }
})(jQuery);