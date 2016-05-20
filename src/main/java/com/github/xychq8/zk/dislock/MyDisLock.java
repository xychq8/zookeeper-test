package com.github.xychq8.zk.dislock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zhangxu on 16/4/25.
 */
public class MyDisLock implements Runnable{
    private static ZkClient zkClient;
    private String prefix;
    private String keyName;

    private static final CountDownLatch latch = new CountDownLatch(10);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                //程序关闭时, 回收所有资源
                if(zkClient != null){
                    zkClient.close();
                }
            }
        }, "MyDisLockShutdownHook"));
    }

    public MyDisLock(int i){
        prefix = "[" + i + "号线程]";
    }

    @Override
    public void run() {
        try {
            zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 30000);
            if(zkClient != null){
                System.out.println(prefix + "成功连接上zookeeper!");
            }

            keyName = zkClient.createEphemeralSequential("/test/dislock", null);
            System.out.println(prefix + "zk路径:" + keyName);
            zkClient.subscribeChildChanges("/test", new IZkChildListener() {
                @Override
                public void handleChildChange(String s, List<String> list) throws Exception {
//                    System.out.println(prefix + "path:" + s + "节点被删除了!!!!!!");
                    if(list.contains(keyName.substring("/test/".length())) && checkIsMin(list)){
                        deleteKey();
                    }
                }
            });

            if(checkIsMin(zkClient.getChildren("/test"))) {
                deleteKey();
            }

            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除key
     */
    private void deleteKey(){
        if(zkClient.delete(keyName)){
            System.out.println(prefix + "key:" + keyName + "已被删除!!!!!!");
            latch.countDown();
        }
    }

    /**
     * 检查是否是所有有序节点中最小的一个
     * @param nodes
     * @return
     */
    private boolean checkIsMin(List<String> nodes){
        int order = nodes.size();
        int seq = Integer.parseInt(keyName.substring("/test/dislock".length()));

        for(String node : nodes){
            int targetSeq = Integer.parseInt(node.substring("dislock".length()));
            if(seq < targetSeq){
                order--;
            }
        }

        if(order == 1){
            return true;
        }

        return false;
    }

}
