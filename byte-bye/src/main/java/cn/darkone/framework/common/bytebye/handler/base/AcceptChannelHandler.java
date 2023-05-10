package cn.darkone.framework.common.bytebye.handler.base;

import cn.darkone.framework.common.bytebye.reactor.SubReactor;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public final class AcceptChannelHandler implements ChannelHandler {
    private final ServerSocketChannel ssc;

    public AcceptChannelHandler(ServerSocketChannel ssc) {
        this.ssc = ssc;
    }

    @Override
    public void run() {
        try {
            SocketChannel sc = ssc.accept();
            if (sc == null) return;
            sc.configureBlocking(false);
            SubReactor.getNext().addRegisterChannel(sc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
