package cn.darkone.framework.common.bytebye.handler.base;

public interface ServiceHandler<T, E> {
    E service(T msg);
}
