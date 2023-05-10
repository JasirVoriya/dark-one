package cn.darkone.framework.common.web.annotations;

import java.lang.annotation.*;

/**
 * 配置Controller类的扫描路径，注解于配置类或启动类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerScan {
    String[] value() default {};
}
