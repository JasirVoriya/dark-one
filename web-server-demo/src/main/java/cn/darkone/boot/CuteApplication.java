package cn.darkone.boot;


import cn.darkone.framework.common.bytebye.Config;
import cn.darkone.framework.common.bytebye.boot.ReactorBootStrap;
import cn.darkone.framework.common.bytebye.handler.HttpRequestDecoder;
import cn.darkone.framework.common.bytebye.handler.HttpResponseEncoder;
import cn.darkone.framework.common.core.utils.Assert;
import cn.darkone.framework.common.web.WebApplicationContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Getter
@Slf4j
public class CuteApplication {
    private final Class<?> primarySource;
    private final ReactorBootStrap reactorBootStrap = ReactorBootStrap.build();

    public CuteApplication(Class<?> primarySource) {
        Assert.notNull(primarySource, "PrimarySources must not be null");
        this.primarySource = primarySource;
    }

    public static WebApplicationContext run(Class<?> primarySource, String... args) {
        return new CuteApplication(primarySource).run(args);
    }

    /**
     * 打算抽象出Application，但是目前只有web，所以先直接返回WebApplication，日後有其他多的應用之後再改
     *
     * @param args
     * @return
     */
    private WebApplicationContext run(String... args) {
        WebApplicationContext.init(primarySource);
//        System.out.println(this.getClass().getClassLoader());

//        启动Http服务器
        DarkDispatcher.getInstance().setPrimarySource(primarySource);
        DarkDispatcher.getInstance().initDark();
        reactorBootStrap.pipelineInitializer(handle -> handle.pipeline()
                        .addLast(new HttpRequestDecoder())
                        .addLast(new HttpServiceHandler())
                        .addLast(new HttpResponseEncoder()))
                .bind(new InetSocketAddress(Config.HOST, Config.PORT));
        log.info("Application started at " + Config.HOST + ":" + Config.PORT);


//// 启动Tomcat:
//        ThreadUtil.submit(() -> {
//            Tomcat tomcat = new Tomcat();
//
//            tomcat.setPort(Integer.getInteger("port", 8080));
//            tomcat.getConnector();
//            // 创建webapp:
//            Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
////            将 启动类 添加到Servlet上下文，供dispatcher使用
//            ctx.getServletContext().setAttribute("primarySource", primarySource);
//
//            WebResourceRoot resources = new StandardRoot(ctx);
//            resources.addPreResources(
//                    new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
//            ctx.setResources(resources);
//            try {
//                tomcat.start();
//            } catch (LifecycleException e) {
//                e.printStackTrace();
//                return;
//            }
//            tomcat.getServer().await();
//        });
        return WebApplicationContext.getInstance();
    }
}
