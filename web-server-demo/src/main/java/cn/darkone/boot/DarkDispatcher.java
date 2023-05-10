package cn.darkone.boot;

import cn.darkone.framework.common.bytebye.http.HttpRequest;
import cn.darkone.framework.common.bytebye.http.HttpResponse;
import cn.darkone.framework.common.core.utils.ResourceUtil;
import cn.darkone.framework.common.web.dispatchers.Dispatcher;
import cn.darkone.framework.common.web.enums.RequestMethod;
import cn.darkone.framework.common.web.scan.ClassScannerFactory;
import cn.darkone.framework.common.web.servlet.DispatcherServlet;
import cn.darkone.framework.common.web.utils.StaticResourceUtil;
import cn.darkone.framework.common.web.init.RegistryInitializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

@Slf4j
public class DarkDispatcher extends DispatcherServlet {
    private static final DarkDispatcher instance = new DarkDispatcher();

    public static DarkDispatcher getInstance() {
        return instance;
    }

    public void initDark() {
        log.info("start initialization dispatcher");
        this.dispatchers = new HashMap<>();
        this.controllers = scanControllers();
        ClassScannerFactory.setGlobalScanner(ClassScannerFactory.getScanner(new String[]{primarySource.getPackage().getName()}));
        this.initializers = scanRegistryInitializers();
        this.initializers.forEach(RegistryInitializer::initializer);
        this.scanDispatchers();

    }


    protected void doGetDark(HttpRequest req, HttpResponse resp) throws IOException, URISyntaxException {
        // 读取当前请求路径:
        String urlPath = req.getPath();

        Dispatcher dispatcher = dispatchers.get(RequestMethod.GET).get(urlPath);

        if (dispatcher != null) {
            dispatcher.invokeDark(req, resp);
        } else if ("/".equals(urlPath)) {
            URL index = ResourceUtil.getResource("index.html");
            if (index == null) return;
            StaticResourceUtil.sendDark(index.toURI(), resp);
        } else {
            // 获取真实文件路径:
            URL resource = ResourceUtil.getResource(urlPath.substring(1));
            if (resource != null) {
                StaticResourceUtil.sendDark(resource.toURI(), resp);
            }
        }
    }

    protected void doPostDark(HttpRequest req, HttpResponse resp) {
        Dispatcher dispatcher = dispatchers.get(RequestMethod.POST).get(req.getPath());
        if (dispatcher != null) {
            dispatcher.invokeDark(req, resp);
        } else {
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


}
