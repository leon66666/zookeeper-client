package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;

/**
 * Created by wangzhongqiu on 2018/5/6.
 * 监听器的一种，NodeCache用于监听Zookeeper数据节点本身发生的变化
 */
public class NodeCacheDemo {
    static String path = "/nodecache";
    static CuratorFramework client = CommonConfig.defaultClient;

    public static void main(String[] args) throws Exception {
        client.start();
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "init".getBytes());
        final NodeCache cache = new NodeCache(client, path, false);
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("Node data update, new data: " +
                        new String(cache.getCurrentData().getData()));
            }
        });
        client.setData().forPath(path, "u".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
