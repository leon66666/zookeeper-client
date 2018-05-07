package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;

/**
 * @author wangzhongqiu
 * @date 2018/5/7.
 * 工具类：能够确保数据节点存在的机制。内部实现了试图创建节点，
 * 如果已经存在那么就不进行任何操作，也不对外抛出异常，否则正常创建数据节点
 */
public class EnsurePathDemo {
    static String path = "/ensurePath/c1";
    static CuratorFramework client = CommonConfig.defaultClient;

    public static void main(String[] args) throws Exception {
        client.start();

        EnsurePath ensurePath = new EnsurePath(path);
        ensurePath.ensure(client.getZookeeperClient());
        ensurePath.ensure(client.getZookeeperClient());

        EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/c1");
        ensurePath2.ensure(client.getZookeeperClient());
    }
}
