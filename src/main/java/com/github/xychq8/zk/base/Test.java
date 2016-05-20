package com.github.xychq8.zk.base;

import org.I0Itec.zkclient.ContentWatcher;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by zhangxu on 16/4/22.
 */
public class Test {

    public static void main(String[] args){
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 30000);

        if(!zkClient.exists("/test")){
            zkClient.createPersistent("/test");
        }

        zkClient.subscribeDataChanges("/test", new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("s:" + s + ",o:" + o);
            }

            public void handleDataDeleted(String s) throws Exception {
                System.out.println("s:" + s);
            }
        });

        zkClient.subscribeChildChanges("/test", new IZkChildListener() {
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("s:" + s + "......");
                for(String children : list){
                    System.out.println(children);
                }
            }
        });

        if(!zkClient.exists("/test/temp")){
            zkClient.createEphemeral("/test/temp");
        }

//        List<String> testChildren =  zkClient.getChildren("/test");
//        for(String children : testChildren){
//            System.out.println(children);
//        }
//
//        zkClient.deleteRecursive("/test");

        zkClient.close();
    }

}
