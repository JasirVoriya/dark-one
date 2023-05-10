package cn.darkone.framework.common.bytebye.reactor;

import cn.darkone.framework.common.bytebye.Config;
import cn.darkone.framework.common.bytebye.handler.base.ChannelHandler;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

@Getter
public abstract class Reactor implements Runnable, Closeable {
    protected Selector selector;
    private long noneLoopCount = 0;

    protected Reactor() throws IOException {
        this.selector = Selector.open();
    }

    protected final void dispatch(SelectionKey key) {
        if (!key.isValid()) return;
        Object att = key.attachment();
        if (att instanceof ChannelHandler) {
            ((ChannelHandler) att).run();
        }
    }

    protected void eventLoopOnce() throws IOException {
        int count = selector.select(Config.SELECT_TIMEOUT);
        if (count == 0) {
            ++noneLoopCount;
//            ThreadUtils.printlnWithThreadName("空轮询：" + noneLoopCount);
            return;
        }
//        ThreadUtils.printlnWithThreadName("监听到 " + count + " 个事件");
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            this.dispatch(key);
        }
//        ThreadUtils.printlnWithThreadName("事件处理完成");
    }
    @Override
    public void run() {
        while (true) {
            try {
                eventLoopOnce();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    @Override
    public void close() throws IOException {
        selector.close();
    }
}
