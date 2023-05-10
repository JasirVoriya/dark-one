package cn.darkone.framework.common.bytebye.boot;

import cn.darkone.framework.common.bytebye.handler.base.PipeLineHandler;
import cn.darkone.framework.common.bytebye.initializer.PipelineInitializer;
import cn.darkone.framework.common.bytebye.reactor.MasterReactor;
import cn.darkone.framework.common.bytebye.reactor.SubReactor;
import cn.darkone.framework.common.bytebye.thread.ThreadUtils;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ReactorBootStrap {
    @Getter
    private MasterReactor masterReactor;

    public ReactorBootStrap pipelineInitializer(PipelineInitializer<PipeLineHandler> handleInitializer) {
        SubReactor.setPipelineInitializer(handleInitializer);
        return this;
    }

    public void bind(InetSocketAddress address) {
        try {
            masterReactor = new MasterReactor();
            masterReactor.bindAndRegister(address);
            masterReactor.start();
        } catch (IOException e) {
            e.printStackTrace();
            ThreadUtils.printlnWithThreadName("服务器启动失败");
        }
    }

    private ReactorBootStrap() {

    }

    public static ReactorBootStrap build() {
        return new ReactorBootStrap();
    }
}
