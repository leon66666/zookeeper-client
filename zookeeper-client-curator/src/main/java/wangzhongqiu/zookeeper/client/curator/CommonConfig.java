package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by wangzhongqiu on 2018/5/6.
 */
public class CommonConfig {
    public static final String NAMESPACE = "test";
    public static final ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
    public static final String LOCALHOST = "127.0.0.1:2181";
    public static final CuratorFramework defaultClient = CuratorFrameworkFactory.builder()
            .connectString(LOCALHOST).retryPolicy(retryPolicy)
            .connectionTimeoutMs(1000)
            .sessionTimeoutMs(1000)
            .namespace(NAMESPACE)//每个对话创建一个隔离的命名空间
            .build();
}
