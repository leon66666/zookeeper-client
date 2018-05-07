package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;

import java.io.File;

/**
 * @author wangzhongqiu
 * @date 2018/5/7.
 * 测试工具类：能够启动一个简易的标准的zookeeper服务，以此来进行一系列的单元测试
 */
public class TestingServerDemo {
    static String path = "/test";

    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer(2181, new File("/home/admin/zk-test-data"));

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(server.getConnectString())
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        System.out.println(client.getChildren().forPath(path));
        server.close();
    }
}
