package cn.darkone.framework.common.bytebye.handler;

import cn.darkone.framework.common.bytebye.http.coder.HttpCoder;
import cn.darkone.framework.common.bytebye.handler.base.ServiceHandler;
import cn.darkone.framework.common.bytebye.http.HttpRequest;

/**
 * 一个Http请求解码器，该解码器只能位于pipeline的首部
 */
public final class HttpRequestDecoder implements ServiceHandler<byte[], HttpRequest> {
    /**
     * @param msg 入站消息，一个byte[]
     * @return 出站消息，一个HttpRequest对象
     * @see HttpRequest
     */
    @Override
    public HttpRequest service(byte[] msg) {
        return HttpCoder.decoder(msg);
    }
}
