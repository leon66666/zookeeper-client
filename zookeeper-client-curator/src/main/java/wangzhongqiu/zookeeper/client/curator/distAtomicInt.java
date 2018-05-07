package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by wangzhongqiu on 2018/5/6.
 * 分布式计数器：指定一个节点作为计数器，
 * 多个机器在分布式锁的控制下，通过更新该节点的数据来实现分布式计数器功能
 */
public class DistAtomicInt {
    static String distatomicint_path = "/distAtomicInt";
    static CuratorFramework client = CommonConfig.defaultClient;

    public static void main(String[] args) throws Exception {
        client.start();
        DistributedAtomicInteger atomicInteger =
                new DistributedAtomicInteger(client, distatomicint_path,
                        new RetryNTimes(3, 1000));
        AtomicValue<Integer> rc = atomicInteger.add(8);
        System.out.println("Result: " + rc.succeeded());
    }
}
