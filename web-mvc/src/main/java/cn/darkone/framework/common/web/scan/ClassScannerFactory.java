package cn.darkone.framework.common.web.scan;

import java.util.HashMap;
import java.util.Map;

public class ClassScannerFactory {

    private static final Map<String[], ClassScanner> factory = new HashMap<>();
    private static ClassScanner globalScanner;


    public static ClassScanner getScanner(String[] scanPaths) {
        if (!factory.containsKey(scanPaths)) {
            factory.put(scanPaths, new ClassScanner(scanPaths));
        }
        return factory.get(scanPaths);
    }

    public static void setGlobalScanner(ClassScanner scanner) {
        globalScanner = scanner;
    }

    public static ClassScanner getGlobalScanner() {
        return globalScanner;
    }
}
