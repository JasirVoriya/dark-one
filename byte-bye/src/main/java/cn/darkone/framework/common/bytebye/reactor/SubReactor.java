package cn.darkone.framework.common.bytebye.reactor;


import cn.darkone.framework.common.bytebye.Config;
import cn.darkone.framework.common.bytebye.initializer.PipelineInitializer;
import cn.darkone.framework.common.bytebye.handler.base.PipeLineHandler;
import cn.darkone.framework.common.bytebye.handler.base.ReadChannelHandler;
import cn.darkone.framework.common.bytebye.thread.ThreadUtils;
import lombok.Setter;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

public class SubReactor extends Reactor {

    private static final Executor subThreadPool = ThreadUtils.newFixedThreadPool(Config.SUB_REACTOR_THREAD_SIZE, "sub-reactor-%d");
    private static final SubReactor[] reactors = new SubReactor[Config.SUB_REACTOR_THREAD_SIZE];
    private static int index = 0;
    private final Queue<SocketChannel> registerQueue = new ArrayBlockingQueue<>(Config.REGISTER_QUEUE_SIZE);
    @Setter
    private static PipelineInitializer<PipeLineHandler> pipelineInitializer;

    static {
        for (int i = 0; i < reactors.length; i++) {
            try {
                reactors[i] = new SubReactor();
                subThreadPool.execute(reactors[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                registerChannel();
                eventLoopOnce();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void addRegisterChannel(SocketChannel channel) {
        registerQueue.add(channel);
    }

    private void registerChannel() throws IOException {
        for (Iterator<SocketChannel> iterator = registerQueue.iterator(); iterator.hasNext(); ) {
            SocketChannel sc = iterator.next();
            iterator.remove();
            SelectionKey selectionKey = sc.register(selector, SelectionKey.OP_WRITE);
            ReadChannelHandler readChannelHandler = new ReadChannelHandler(selectionKey);
            pipelineInitializer.initHandle(readChannelHandler);
            selectionKey.attach(readChannelHandler);
//            ThreadUtils.printlnWithThreadName("注册成功：" + sc.getRemoteAddress());
        }
    }

    private SubReactor() throws IOException {
    }

    public static SubReactor getNext() {
        SubReactor reactor = reactors[index];
        index = (index + 1) % Config.SUB_REACTOR_THREAD_SIZE;
        return reactor;
    }
}
