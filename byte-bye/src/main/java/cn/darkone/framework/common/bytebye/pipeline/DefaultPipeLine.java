package cn.darkone.framework.common.bytebye.pipeline;

import cn.darkone.framework.common.bytebye.handler.base.ServiceHandler;

import java.util.LinkedList;
import java.util.List;
public class DefaultPipeLine implements ServiceHandler<byte[], byte[]> {
    private final List<ServiceHandler<?, ?>> pipeline = new LinkedList<>();

    public DefaultPipeLine addLast(ServiceHandler<?, ?> handler) {
        pipeline.add(handler);
        return this;
    }

    /**
     * 处理流水线
     *
     * @param msg 入站消息，一个byte[]
     * @return 出站消息，一个byte[]
     */
    @Override
    public byte[] service(byte[] msg) {
        Object message =  msg;
        for (ServiceHandler handler : pipeline) {
            if (message == null) return null;
            message = handler.service(message);
        }
        return (byte[]) message;
    }
}
