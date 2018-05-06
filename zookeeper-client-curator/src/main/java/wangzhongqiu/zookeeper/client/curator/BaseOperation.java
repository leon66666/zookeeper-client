package wangzhongqiu.zookeeper.client.curator;

import com.google.common.io.Closeables;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created by wangzhongqiu on 2018/5/6.
 * 基本操作：创建会话，创建节点，删除节点，获取节点存储内容，更新节点存储的内容
 */
public class BaseOperation {
    public static final String PATH = "/basictest";

    public static void main(String[] args) {
        //创建会话对象
        CuratorFramework client = CommonConfig.defaultClient;
        //开始会话
        client.start();
        try {
            //创建一个节点，初始内容为空
            client.create().forPath(PATH);
            //创建一个节点，附带初始内容
            client.create().forPath(PATH, "basictest1".getBytes());
            //创建一个临时节点，附带初始内容
            client.create().withMode(CreateMode.EPHEMERAL).forPath(PATH, "basictest2".getBytes());
            //创建一个临时节点，父节点不存在，递归创建父节点，创建的父节点都是持久节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(PATH, "basictest3".getBytes());

            //关闭会话
            Closeables.close(client, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(CuratorFramework client, String path) throws Exception {
        //删除一个节点
        client.delete().forPath(path);
        //删除一个节点，递归删除子节点
        client.delete().deletingChildrenIfNeeded().forPath(path);
        //删除一个节点，强制指定版本进行删除
        client.delete().withVersion(1).forPath(path);
        //删除一个节点，强制保证删除。Curator引入了重试机制，一旦删除失败，会在后台反复重试直到删除成功
        client.delete().guaranteed().forPath(path);
        return;
    }

    public static String get(CuratorFramework client, String path) throws Exception {
        //读取一个节点的内容
        String str = new String(client.getData().forPath(path));
        //读取一个节点的内容，同时获取到该节点的stat
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        return str;
    }

    public static Stat set(CuratorFramework client, String path) throws Exception {
        Stat stat = new Stat();
        //更新一个节点的数据内容，返回Stat对象
        stat = client.setData().forPath(path);
        //更新一个节点的数据内容，强制指定版本进行更新，用来实现CAS
        stat = client.setData().withVersion(1).forPath(path);
        return stat;
    }
}
