package cn.darkone.framework.common.web.dispatchers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class GetDispatcher extends Dispatcher {

    public GetDispatcher(Object instance, Method method) {
        super(instance, method);
    }

    @Override
    protected Object[] getArguments(HttpServletRequest request, HttpServletResponse response) {
        return super.getArguments(request,response);
    }
}
