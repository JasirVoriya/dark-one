package cn.darkone.framework.common.web.dispatchers;

import cn.darkone.framework.common.web.enums.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DispatcherFactory {
    private static final Map<RequestMethod, Map<Method, Dispatcher>> factory = new HashMap<>();

    static {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            factory.put(requestMethod, new HashMap<>());
        }
    }

    public static Dispatcher getDispatcher(RequestMethod requestMethod, Object controller, Method method) {
        switch (requestMethod) {
            case GET:
                if (!factory.get(requestMethod).containsKey(method))
                    factory.get(requestMethod).put(method, new GetDispatcher(controller, method));
                return factory.get(requestMethod).get(method);
            case HEAD:
                break;
            case POST:
                if (!factory.get(requestMethod).containsKey(method))
                    factory.get(requestMethod).put(method, new PostDispatcher(controller, method));
                return factory.get(requestMethod).get(method);
            case PUT:
                break;
            case PATCH:
                break;
            case DELETE:
                break;
            case OPTIONS:
                break;
            case TRACE:
                break;
        }
        return null;
    }
}
