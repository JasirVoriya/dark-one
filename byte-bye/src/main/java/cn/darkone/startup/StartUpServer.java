package cn.darkone.startup;

import cn.darkone.framework.common.bytebye.Config;
import cn.darkone.framework.common.bytebye.boot.ReactorBootStrap;
import cn.darkone.framework.common.bytebye.handler.HttpRequestDecoder;
import cn.darkone.framework.common.bytebye.handler.HttpResponseEncoder;

import java.net.InetSocketAddress;

public class StartUpServer {
    public static void main(String[] args) {

        ReactorBootStrap reactorBootStrap = ReactorBootStrap.build();
        reactorBootStrap.pipelineInitializer(handle -> handle.pipeline()
                        .addLast(new HttpRequestDecoder())//http解码器
                        .addLast(new HttpServiceHandler())//http业务处理
                        .addLast(new HttpResponseEncoder()))//http编码器
                .bind(new InetSocketAddress(Config.HOST, Config.PORT));
    }
}
