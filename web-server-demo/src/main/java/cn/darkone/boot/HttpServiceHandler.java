package cn.darkone.boot;

import cn.darkone.framework.common.bytebye.handler.base.ServiceHandler;
import cn.darkone.framework.common.bytebye.http.HttpRequest;
import cn.darkone.framework.common.bytebye.http.HttpResponse;
import cn.darkone.framework.common.bytebye.http.enums.HttpHeaderKeys;
import cn.darkone.framework.common.bytebye.http.enums.HttpHeaderValues;
import cn.darkone.framework.common.bytebye.http.enums.HttpStatus;
import cn.darkone.framework.common.bytebye.http.enums.HttpVersion;
import lombok.SneakyThrows;

public class HttpServiceHandler implements ServiceHandler<HttpRequest, HttpResponse> {
    /**
     * @param request 入站消息，一个HttpRequest对象
     * @return 出站消息，一个HttpResponse对象
     * @see HttpRequest
     * @see HttpResponse
     */
    @SneakyThrows
    @Override
    public HttpResponse service(HttpRequest request) {


        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.OK);
        response.setVersion(HttpVersion.HTTP_1_1);
        response.addHeader(HttpHeaderKeys.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);

        switch (request.getMethod()) {
            case "GET":
                DarkDispatcher.getInstance().doGetDark(request, response);
                break;
            case "POST":
                DarkDispatcher.getInstance().doPostDark(request, response);
                break;
        }
        return response;
    }
}
