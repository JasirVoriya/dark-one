package cn.darkone.framework.common.web.dispatchers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class PostDispatcher extends Dispatcher {

    public PostDispatcher(Object instance, Method method) {
        super(instance, method);
    }


    @Override
    protected Object[] getArguments(HttpServletRequest request, HttpServletResponse response) {
        // TODO: 2022/7/7 post请求的参数解析还没有完全写完 ，只是完全基于get请求
        return super.getArguments(request,response);
    }
}
