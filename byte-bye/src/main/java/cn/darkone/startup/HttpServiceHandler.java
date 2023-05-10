package cn.darkone.startup;

import cn.darkone.framework.common.bytebye.handler.AbstractHttpServiceHandler;
import cn.darkone.framework.common.bytebye.http.HttpRequest;
import cn.darkone.framework.common.bytebye.http.HttpResponse;
import cn.darkone.framework.common.bytebye.http.enums.HttpHeaderKeys;
import cn.darkone.framework.common.bytebye.http.enums.HttpHeaderValues;
import cn.darkone.framework.common.bytebye.http.enums.HttpStatus;
import cn.darkone.framework.common.bytebye.http.enums.HttpVersion;

import java.nio.charset.StandardCharsets;

public class HttpServiceHandler extends AbstractHttpServiceHandler {
    /**
     * @param msg 入站消息，一个HttpRequest对象
     * @return 出站消息，一个HttpResponse对象
     * @see HttpRequest
     * @see HttpResponse
     */
    @Override
    public HttpResponse service(HttpRequest msg) {
        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.OK);
        response.setVersion(HttpVersion.HTTP_1_1);
        response.addHeader(HttpHeaderKeys.CONTENT_TYPE, HttpHeaderValues.TEXT_HTML);
        response.addHeader(HttpHeaderKeys.CONNECTION, HttpHeaderValues.CLOSE);
        String body = "<html><body>" +
                msg.getMsg().replace("\r\n", "<br>") +
                "</body></html>";
        response.setBody(body.getBytes(StandardCharsets.UTF_8));
        return response;
    }
}
