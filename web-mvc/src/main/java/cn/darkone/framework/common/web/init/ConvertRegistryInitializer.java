package cn.darkone.framework.common.web.init;

import cn.darkone.framework.common.web.scan.ClassScanner;
import cn.darkone.framework.common.web.scan.ClassScannerFactory;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.Array;

/**
 * 属性转换器注册初始化
 */
public class ConvertRegistryInitializer implements RegistryInitializer {

    @Override
    public void initializer() {

        Converter enumConverter = new Converter() {
            @SneakyThrows
            public <T> T convert(Class<T> clazz, Object value) {
                if (value instanceof String) {
                    return clazz.cast(MethodUtils.invokeStaticMethod(clazz, "valueOf", value));
                }
                if (value instanceof String[]) {
                    return clazz.cast(MethodUtils.invokeStaticMethod(clazz, "valueOf", ((String[]) value)[0]));
                }
                return clazz.cast(value);
            }
        };
        Converter enumArrayConverter = new Converter() {
            @SneakyThrows
            @Override
            public <T> T convert(Class<T> clazz, Object value) {
                if (value instanceof String[]) {
                    String[] values = (String[]) value;
                    Object enums = Array.newInstance(clazz.getComponentType(), values.length);
                    for (int i = 0; i < values.length; i++) {
                        Array.set(enums, i, MethodUtils.invokeStaticMethod(clazz.getComponentType(), "valueOf", values[i]));
                    }
                    return clazz.cast(enums);
                }
                return clazz.cast(value);
            }
        };
        ClassScanner scanner = ClassScannerFactory.getGlobalScanner();

        for (var enumClazz : scanner.getReflections().getSubTypesOf(Enum.class)) {
            ConvertUtils.register(enumConverter, enumClazz);
            ConvertUtils.register(enumArrayConverter, Array.newInstance(enumClazz,0).getClass());
        }
    }
}
