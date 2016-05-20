package com.github.xychq8.zk.watch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by zhangxu on 16/4/22.
 */
public class ChildChangeTest {
    private static ZkClient zkClient = new ZkClient("127.0.0.1:2181", 30000);

    public static void main(String[] args) throws Exception{
        zkClient.deleteRecursive("/test");
        zkClient.createPersistent("/test");
        zkClient.createPersistent("/test/pers");

        zkClient.subscribeChildChanges("/test", new IZkChildListener() {
            public void handleChildChange(String path, List<String> list) throws Exception {
                System.out.println("path:" + path);
                for(String str : list){
                    System.out.println(str);
                }
            }
        });

        zkClient.delete("/test/pers");

        Thread.sleep(1000);
        zkClient.close();
    }

}
