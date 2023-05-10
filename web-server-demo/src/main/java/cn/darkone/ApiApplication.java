package cn.darkone;

import cn.darkone.boot.CuteApplication;
import cn.darkone.framework.common.web.WebApplicationContext;
import cn.darkone.framework.common.web.annotations.ControllerScan;


@ControllerScan("cn.darkone.controller")
public class ApiApplication {
    public static void main(String[] args) {
        WebApplicationContext context = CuteApplication.run(ApiApplication.class, args);
    }
}
