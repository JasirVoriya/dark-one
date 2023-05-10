package cn.darkone.framework.common.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CuteBeanUtils {
    public static <T> boolean setter(T instance, String att, Object value, Class<?> type) {
        try {
            Method met = instance.getClass().getMethod("set" + StringUtils.firstToUpperCase(att), type);    // 得到setter方法
            met.invoke(instance, value);    // 设置setter的内容
            return true;
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            return false;
        }
    }

    public static Method getter(Object obj, String att) throws NoSuchMethodException {
        return obj.getClass().getMethod("get" + StringUtils.firstToUpperCase(att));
    }

    public static <T> T newInstance(Class<T> cls) {
        try {
            return cls.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getDeclaredField(Class<?> cls, String name) {
        try {
            return cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
    /**
     * 判断是否是基础数据类型，即 int,double,long等类似格式
     */
    public static boolean isCommonDataType(Class<?> clazz){
        return clazz.isPrimitive();
    }
    /**
     * 判断是否是基础数据类型的包装类型
     *
     */
    public static boolean isWrapClass(Class<?> clz) {
        try {
            return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 获取启动类的class
     * @return 启动类的Class实例
     */
    public static Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        }
        catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }
}
