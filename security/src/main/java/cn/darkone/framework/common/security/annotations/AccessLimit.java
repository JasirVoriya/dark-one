package cn.darkone.framework.common.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    //单位时间
    int seconds();

    //    最大次数
    int maxCount();

    //是否需要登录
    boolean needLogin() default true;
}