package wangzhongqiu.zookeeper.client.curator;

import com.google.common.io.Closeables;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangzhongqiu on 2018/5/6.
 * 异步操作。
 */
public class BackgroundOperation {
    static String path = "/backgroundtest";
    static CuratorFramework client = CommonConfig.defaultClient;
    static CountDownLatch semaphore = new CountDownLatch(2);
    static ExecutorService tp = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        try {
            client.start();
            System.out.println("Main thread: " + Thread.currentThread().getName());
            // 此处传入了自定义的Executor
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println("event[code: " + event.getResultCode() + ", type: " + event.getType() + "]");
                    System.out.println("Thread of processResult: " + Thread.currentThread().getName());
                    semaphore.countDown();
                }
            }, tp).forPath(path, "init1".getBytes());
            // 此处没有传入自定义的Executor
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println("event[code: " + event.getResultCode() + ", type: " + event.getType() + "]");
                    System.out.println("Thread of processResult: " + Thread.currentThread().getName());
                    semaphore.countDown();
                }
            }).forPath(path, "init2".getBytes());
            semaphore.await();
            Closeables.close(client, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tp.shutdown();
        }
    }
}
