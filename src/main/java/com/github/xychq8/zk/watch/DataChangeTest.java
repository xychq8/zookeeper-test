package com.github.xychq8.zk.watch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * Created by zhangxu on 16/4/22.
 */
public class DataChangeTest {
    private static ZkClient zkClient = new ZkClient("127.0.0.1:2181", 30000);

    public static void main(String[] args) throws Exception{
        zkClient.deleteRecursive("/test");
        zkClient.createPersistent("/test");

        zkClient.subscribeDataChanges("/test", new IZkDataListener() {
            public void handleDataChange(String path, Object content) throws Exception {
                System.out.println("path:" + path + ",content:" + content);
            }

            public void handleDataDeleted(String path) throws Exception {
                System.out.println("path:" + path);
            }
        });

        zkClient.writeData("/test", "test");
        zkClient.deleteRecursive("/test");

        Thread.sleep(1000);
        zkClient.close();
    }

}
