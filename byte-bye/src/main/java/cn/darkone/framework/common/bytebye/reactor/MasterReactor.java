package cn.darkone.framework.common.bytebye.reactor;

import cn.darkone.framework.common.bytebye.handler.base.AcceptChannelHandler;
import cn.darkone.framework.common.bytebye.thread.ThreadUtils;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executor;

@Getter
public class MasterReactor extends Reactor {
    //    private final Class<? extends ReadHandler> readHandlerCls;
    private ServerSocketChannel ssc;
    private static final Executor threadPool = ThreadUtils.newFixedThreadPool(1, "mas-reactor-%d");
    private AcceptChannelHandler acceptHandler;

    public MasterReactor() throws IOException {
//        this.readHandlerCls = readHandlerCls;
    }

    @Override
    protected void eventLoopOnce() {
        acceptHandler.run();
    }

    public void bindAndRegister(InetSocketAddress bindAddress) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(bindAddress);
        acceptHandler = new AcceptChannelHandler(ssc);
//        设置通道非阻塞
//        ssc.configureBlocking(false);
//        选择器注册到ssc通道上，负该通道的连接操作
//        ssc.register(selector, SelectionKey.OP_ACCEPT, acceptHandler);
    }

    public void start() {
        threadPool.execute(this);
    }
}
