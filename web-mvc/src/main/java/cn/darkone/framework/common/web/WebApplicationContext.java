package cn.darkone.framework.common.web;

import cn.darkone.framework.common.core.utils.ResourceUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileInputStream;
import java.io.InputStream;

@Getter
@Slf4j
public class WebApplicationContext {
    private Class<?> primarySource;
    private static volatile WebApplicationContext instance = null;
    private static boolean isInit;


    private WebApplicationContext() {

    }

    public static WebApplicationContext getInstance() {
        if (instance == null) {
            // 加锁目的，防止多线程同时进入造成对象多次实例化
            synchronized (WebApplicationContext.class) {
                // 为了在null的情况下创建实例，防止不必要的损耗
                if (instance == null) {
                    instance = new WebApplicationContext();
                }
            }
        }
        // 返回实例对象
        return instance;
    }

    @SneakyThrows
    private static void printBanner(){
        InputStream stream = ResourceUtil.getResourceStream("banner.txt");
        String banner = new String(stream.readAllBytes());
        FileInputStream fis = new FileInputStream("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(fis);
        System.out.println("\033[1;92m" + banner + "\nv" + model.getVersion() + "\033[0m ");
    }

    public static void init(Class<?> primarySource) {
        if (!isInit) {
            printBanner();
            getInstance();
            instance.primarySource = primarySource;
            isInit = true;
            log.info("web-mvc init initialization completed");
        }
    }
}
