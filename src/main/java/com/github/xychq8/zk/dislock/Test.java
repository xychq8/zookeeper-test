package com.github.xychq8.zk.dislock;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zhangxu on 16/4/25.
 */
public class Test {

    public static void main(String[] args) throws Exception{
        for(int i = 1; i <= 10; i++){
            new Thread(new MyDisLock(i)).start();
        }

        Thread.sleep(Long.MAX_VALUE);
    }

}
