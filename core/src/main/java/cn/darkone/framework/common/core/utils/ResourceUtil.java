package cn.darkone.framework.common.core.utils;

import java.io.InputStream;
import java.net.URL;


/**
 * 通过获取当前类加载器的资源文件
 */
public class ResourceUtil {
    public static URL getResource(String resourcePath){
        return ResourceUtil.class.getClassLoader().getResource(resourcePath);
    }

    public static InputStream getResourceStream(String resourcePath){
        return ResourceUtil.class.getClassLoader().getResourceAsStream(resourcePath);
    }
}
