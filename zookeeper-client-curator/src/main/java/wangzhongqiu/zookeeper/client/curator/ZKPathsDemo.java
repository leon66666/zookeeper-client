package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author wangzhongqiu
 * @date 2018/5/7.
 * 工具类：ZKPaths，提供了一些简答的API来构建Znode路径、递归创建和删除节点等。使用方式非常方便
 */
public class ZKPathsDemo {
    static String path = "/zkpath";
    static CuratorFramework client = CommonConfig.defaultClient;

    public static void main(String[] args) throws Exception {
        client.start();
        ZooKeeper zookeeper = client.getZookeeperClient().getZooKeeper();

        System.out.println(ZKPaths.fixForNamespace(path, "sub"));
        System.out.println(ZKPaths.makePath(path, "sub"));
        System.out.println(ZKPaths.getNodeFromPath("/curator_zkpath_sample/sub1"));

        ZKPaths.PathAndNode pn = ZKPaths.getPathAndNode("/curator_zkpath_sample/sub1");
        System.out.println(pn.getPath());
        System.out.println(pn.getNode());

        String dir1 = path + "/child1";
        String dir2 = path + "/child2";
        ZKPaths.mkdirs(zookeeper, dir1);
        ZKPaths.mkdirs(zookeeper, dir2);
        System.out.println(ZKPaths.getSortedChildren(zookeeper, path));

        ZKPaths.deleteChildren(client.getZookeeperClient().getZooKeeper(), path, true);
    }
}
