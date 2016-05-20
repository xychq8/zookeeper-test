package com.github.xychq8.zk.watch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * Created by zhangxu on 16/4/22.
 */
public class StateChangeTest {
    private static ZkClient zkClient = new ZkClient("127.0.0.1:2181", 30000);

    public static void main(String[] args) throws Exception{
        zkClient.subscribeStateChanges(new IZkStateListener() {
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                System.out.println(keeperState);
            }

            public void handleNewSession() throws Exception {
                System.out.println("new session!");
            }

            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {
                System.out.println(throwable.getMessage());
            }
        });

        Thread.sleep(100000000);
        zkClient.close();
    }

}
