package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by wangzhongqiu on 2018/5/6.
 * 分布式Barrier：分为主动放开栅栏和达到指定数量自动放开栅栏
 */
public class Barrier {
    static String barrier_path = "/barrier";
    static DistributedBarrier barrier;
    static CuratorFramework client = CommonConfig.defaultClient;

    public static void main(String[] args) throws Exception {
        atoBarrier();
    }

    public static void barrier() throws Exception {
        barrier = new DistributedBarrier(client, barrier_path);
        client.start();
        barrier.setBarrier();
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "号barrier设置");
                        barrier.waitOnBarrier();
                        System.err.println("启动...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
        barrier.removeBarrier();
    }

    public static void atoBarrier() throws Exception {
        client.start();
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrier_path, 5);
                        System.out.println(Thread.currentThread().getName() + "号barrier设置");
                        barrier.enter();
                        System.err.println("启动...");
                        barrier.leave();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
    }
}
