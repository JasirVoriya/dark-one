package cn.darkone.framework.common.web.annotations;

import cn.darkone.framework.common.web.enums.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostMapping {
    /**
     * 请求路径
     */
    String[] value() default {"/"};


    /**
     * 请求方法
     * @see RequestMethod
     */
    RequestMethod[] method() default {RequestMethod.POST};

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};
}
