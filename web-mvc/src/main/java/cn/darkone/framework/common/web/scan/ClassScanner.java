package cn.darkone.framework.common.web.scan;

import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;


@Getter
public class ClassScanner {
    private final Reflections reflections;

    /**
     * 扫描带有指定注解的类
     * @param annotation 指定注解
     * @return 扫描出来的类集合
     */
    public Set<Class<?>> scanTypesAnnotatedWith(Class<? extends Annotation> annotation){
        return reflections.getTypesAnnotatedWith(annotation);
    }
    public ClassScanner(String[] scanPaths){
        this.reflections=new Reflections(new ConfigurationBuilder()
                .forPackages(scanPaths) // 指定路径URL
                .addScanners(new SubTypesScanner()) // 添加子类扫描工具
                .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
                .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
                .addScanners(new MethodParameterScanner() ) // 添加方法参数扫描工具
        );
    }
}
