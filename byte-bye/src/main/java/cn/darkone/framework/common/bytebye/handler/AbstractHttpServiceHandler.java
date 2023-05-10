package cn.darkone.framework.common.bytebye.handler;

import cn.darkone.framework.common.bytebye.handler.base.ServiceHandler;
import cn.darkone.framework.common.bytebye.http.HttpRequest;
import cn.darkone.framework.common.bytebye.http.HttpResponse;

public abstract class AbstractHttpServiceHandler implements ServiceHandler<HttpRequest, HttpResponse> {

    public abstract HttpResponse service(HttpRequest msg);
}
