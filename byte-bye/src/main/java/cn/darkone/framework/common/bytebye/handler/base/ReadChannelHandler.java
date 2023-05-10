package cn.darkone.framework.common.bytebye.handler.base;

import cn.darkone.framework.common.bytebye.Config;
import cn.darkone.framework.common.bytebye.pipeline.DefaultPipeLine;
import cn.darkone.framework.common.bytebye.thread.ThreadUtils;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

@Getter
public final class ReadChannelHandler implements ChannelHandler, PipeLineHandler, Closeable {
    private final DefaultPipeLine pipeline = new DefaultPipeLine();
    private final SelectionKey key;
    private final SocketChannel sc;
    //    private String msg = "";
    private final List<Byte> msg = new LinkedList<>();
    private byte[] bytes;
    private static final Executor readThreadPool = ThreadUtils.newFixedThreadPool(Config.SERVICE_HANDLER_THREAD_SIZE, "service-handler-%d");
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(Config.BUF_SIZE);

    public ReadChannelHandler(SelectionKey key) {
        this.key = key;
        this.sc = (SocketChannel) key.channel();
    }

    @Override
    public void run() {
        if (isValid() && readChannelComplete()) {
            // TODO: 2022/7/14 有线程安全问题待解决，所以先不启用业务线程池
//            readThreadPool.execute(() -> {
            bytes = new byte[msg.size()];
            for (int i = 0; i < msg.size(); i++) bytes[i] = msg.get(i);
            Object result = pipeline().service(bytes);
//            Object result = bytes;
            if (result != null) {
                writeChannel((byte[]) result);
            }
            this.close();
//            });
        }
    }


    private boolean readChannelComplete() {
        if (!isValid()) return true;
        try {
            synchronized (sc) {
                if (isValid()) {
                    buffer.clear();
                    int len = sc.read(buffer);
                    if (!isValid() || len <= 0) return true;
                    buffer.flip();
                    for (int i = 0, limit = buffer.limit(); i < limit; i++) {
                        msg.add(buffer.get(i));
                    }
//                key.interestOps(SelectionKey.OP_WRITE);
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void writeChannel(byte[] response) {
        if (!isValid()) return;
        try {
            synchronized (sc) {
                if (!isValid()) return;
                sc.write(ByteBuffer.wrap(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        return key.isValid() && sc.isConnected();
    }

    @Override
    public void close() {
        try {
            synchronized (sc) {
                key.cancel();
                sc.socket().close();
                sc.close();
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public DefaultPipeLine pipeline() {
        return pipeline;
    }
}
