package cn.darkone.framework.common.web.init;

/**
 * 注册器接口，该接口的所有实现类将在 DispatcherServlet 初始化的时候执行 initializer()
 */
public interface RegistryInitializer {
    void initializer();
}
