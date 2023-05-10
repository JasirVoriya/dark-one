package cn.darkone.framework.common.bytebye.handler;

import cn.darkone.framework.common.bytebye.http.coder.HttpCoder;
import cn.darkone.framework.common.bytebye.handler.base.ServiceHandler;
import cn.darkone.framework.common.bytebye.http.HttpResponse;

/**
 * 一个Http响应编码器，该编码器只能位于pipeline的尾部
 */
public final class HttpResponseEncoder implements ServiceHandler<HttpResponse,byte[]> {
    /**
     * @param msg 入站消息，一个HttpResponse对象
     * @return 出站消息，一个byte[]
     * @see HttpResponse
     */
    @Override
    public byte[] service(HttpResponse msg) {
        return HttpCoder.encoder(msg);
    }
}
