package cn.darkone.framework.common.web.servlet;


import cn.darkone.framework.common.core.utils.Assert;
import cn.darkone.framework.common.core.utils.CuteBeanUtils;
import cn.darkone.framework.common.web.annotations.ControllerScan;
import cn.darkone.framework.common.web.annotations.RestController;
import cn.darkone.framework.common.web.dispatchers.Dispatcher;
import cn.darkone.framework.common.web.enums.RequestMethod;
import cn.darkone.framework.common.web.init.RegistryInitializer;
import cn.darkone.framework.common.web.scan.ClassScanner;
import cn.darkone.framework.common.web.scan.ClassScannerFactory;
import cn.darkone.framework.common.web.servlet.base.AbstractController;
import cn.darkone.framework.common.web.utils.MappingScanUtil;
import cn.darkone.framework.common.web.utils.StaticResourceUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/")
@NoArgsConstructor
@Getter
@Slf4j
public class DispatcherServlet extends AbstractController {
    protected Object[] controllers;
    @Setter
    protected Class<?> primarySource;
    protected Map<RequestMethod, Map<String, Dispatcher>> dispatchers;
    protected List<RegistryInitializer> initializers;

    /**
     * 从启动类里获取Controller的扫描路径
     *
     * @return Controller扫描路径
     */
    private String[] getControllerScanPath() {
        Assert.notNull(primarySource, "the primary bean sources must not be null");
//        从启动类里获取Controller的扫描路径
        ControllerScan controllerScan = primarySource.getAnnotation(ControllerScan.class);
        if (controllerScan == null) {
            return new String[]{primarySource.getPackage().getName()};
        }
        return controllerScan.value();
    }

    /**
     * 扫描指定路径下面的controller类
     *
     * @return 返回所有controller类的反射对象
     */
    protected Object[] scanControllers() {
        log.info("scan controllers completed");
        String[] scanPaths = getControllerScanPath();
        Object[] controllers = ClassScannerFactory.getScanner(scanPaths).scanTypesAnnotatedWith(RestController.class).stream().map(CuteBeanUtils::newInstance).toArray();
        log.info("scan controllers completed,find {} controllers", controllers.length);
        return controllers;
    }

    /**
     * 扫描所有分发器
     */
    protected void scanDispatchers() {
        Assert.notNull(controllers, "the controllers must not be null,please call scanControllers method in Constructors first");
        for (Object controller : controllers) {
            this.dispatchers.putAll(MappingScanUtil.getDispatchers(controller));
        }
    }

    /**
     * 扫描所有注册器
     *
     * @return 注册器列表
     */
    protected List<RegistryInitializer> scanRegistryInitializers() {
        ClassScanner scanner = ClassScannerFactory.getGlobalScanner();
        List<RegistryInitializer> registryInitializers = scanner.getReflections().getSubTypesOf(RegistryInitializer.class).stream().map(CuteBeanUtils::newInstance).collect(Collectors.toList());
        log.info("scan registry initialization completed");
        return registryInitializers;
    }

    protected String getUrlPath(HttpServletRequest req) {
        // 读取当前请求路径:
        ServletContext ctx = req.getServletContext();
        // RequestURI包含ContextPath,需要去掉:
        return req.getRequestURI().substring(ctx.getContextPath().length());
    }

    @Override
    public void init() {
        System.out.println(this.getClass().getClassLoader());
        this.primarySource = (Class<?>) getServletContext().getAttribute("primarySource");
        this.dispatchers = new HashMap<>();
        this.controllers = scanControllers();
        ClassScannerFactory.setGlobalScanner(ClassScannerFactory.getScanner(new String[]{primarySource.getPackage().getName()}));
        this.initializers = scanRegistryInitializers();
        this.initializers.forEach(RegistryInitializer::initializer);
        this.scanDispatchers();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getMethod();

        // 读取当前请求路径:
        ServletContext ctx = req.getServletContext();
        // RequestURI包含ContextPath,需要去掉:
        String urlPath = req.getRequestURI().substring(ctx.getContextPath().length());

        Dispatcher dispatcher = dispatchers.get(RequestMethod.GET).get(urlPath);

        if (dispatcher != null) {
            dispatcher.invoke(req, resp);
        } else {
            // 获取真实文件路径:
            String filepath = ctx.getRealPath(urlPath);
            filepath += "/".equals(urlPath) ? "index.html" : "";
            StaticResourceUtil.send(filepath, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Dispatcher dispatcher = dispatchers.get(RequestMethod.POST).get(this.getUrlPath(req));
        if (dispatcher != null) {
            dispatcher.invoke(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

}
