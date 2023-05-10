package cn.darkone.framework.common.web.utils;

import cn.darkone.framework.common.web.annotations.GetMapping;
import cn.darkone.framework.common.web.annotations.PostMapping;
import cn.darkone.framework.common.web.annotations.RequestMapping;
import cn.darkone.framework.common.web.annotations.RestController;
import cn.darkone.framework.common.web.dispatchers.Dispatcher;
import cn.darkone.framework.common.web.dispatchers.DispatcherFactory;
import cn.darkone.framework.common.web.enums.RequestMethod;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MappingScanUtil {

    private static final Class<? extends Annotation>[] mappingAnnotations = new Class[]{RequestMapping.class, GetMapping.class, PostMapping.class};


    public static Map<RequestMethod, Map<String, Dispatcher>> getDispatchers(Object controller) {
        Class<?> controllerCls = controller.getClass();
        RequestMapping requestMapping = controllerCls.getAnnotation(RequestMapping.class);
        Assert.notNull(requestMapping, "the " + controllerCls.getTypeName() + " must not be annotated with " + RequestMapping.class.getTypeName());
//            类访问路径
        String[] controllerPaths = requestMapping.value();
//        Mapping方法和对应的Mapping注解
        Map<Method, Set<Annotation>> methods = getMappingMethods(controllerCls);


        Map<RequestMethod, Map<String, Dispatcher>> dispatchers = new HashMap<>();
        for (RequestMethod requestMethod : RequestMethod.values()) {
            dispatchers.put(requestMethod, new HashMap<>());
        }

        methods.forEach((method, annotations) -> {
            for (Annotation annotation : annotations) {
//                请求路径
                String[] paths = new String[0];
//                请求方法
                RequestMethod[] requestMethods = new RequestMethod[0];
                try {
                    paths = (String[]) annotation.getClass().getMethod("value").invoke(annotation);
                    requestMethods = (RequestMethod[]) annotation.getClass().getMethod("method").invoke(annotation);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
//              构造分发器
                for (String controllerPath : controllerPaths) {
                    for (String path : paths) {
                        for (RequestMethod requestMethod : requestMethods) {
                            dispatchers.get(requestMethod).put(controllerPath + path, DispatcherFactory.getDispatcher(requestMethod, controller, method));
                        }
                    }
                }

            }
        });
        return dispatchers;
    }

    /**
     * 扫描Mapping方法和对应的Mapping注解
     *
     * @param controller 被扫描的Controller类
     * @return Mapping方法和对应的Mapping注解
     */
    private static Map<Method, Set<Annotation>> getMappingMethods(Class<?> controller) {
        Assert.isTrue(controller.isAnnotationPresent(RestController.class), "the " + controller.getTypeName() + " is not a restController");
        Map<Method, Set<Annotation>> methods = new LinkedHashMap<>();
//        遍历所有方法
        for (Method method : controller.getMethods()) {
            Set<Annotation> annotations = new LinkedHashSet<>();
//            获取该方法的所有Mapping注解
            for (Class<? extends Annotation> aClass : mappingAnnotations) {
                if (method.isAnnotationPresent(aClass)) {
                    annotations.add(method.getAnnotation(aClass));

                }
            }
            if (annotations.size() != 0) {
                methods.put(method, annotations);
            }
        }
        return methods;
    }

}
