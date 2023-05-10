package cn.darkone.framework.common.bytebye.thread;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadUtils {
    public synchronized static void printlnWithThreadName(String str) {
        System.out.println(Thread.currentThread().getName() + " >> " + str);
    }

    public static Executor newFixedThreadPool(int nThreads, String nameFormat) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(nameFormat).build();
        return Executors.newFixedThreadPool(nThreads, namedThreadFactory);
    }
}
