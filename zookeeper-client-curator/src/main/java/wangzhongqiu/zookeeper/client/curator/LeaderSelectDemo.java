package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by wangzhongqiu on 2018/5/6.
 * Master选举。选择一个根节点，多台机器同时创建一个自己节点/lock。
 * 利用zookeeper的特性，最终只能有一个创建成功
 * 成功获取到Master权利之后，会回调监听器的takeLeaderShip()方法，
 * 一旦方式执行完毕，会立即释放掉Master权利，监听器需要开发者自定义实现
 * 当一个应用实例称为Master之后，其他竞选者会进入等待，
 * 直到当前Master挂了或退出后才开始下一轮的竞选
 */
public class LeaderSelectDemo {
    static String master_path = "/masterSelect";

    static CuratorFramework client = CommonConfig.defaultClient;

    public static void main(String[] args) throws Exception {
        client.start();
        LeaderSelector selector = new LeaderSelector(client,
                master_path,
                new LeaderSelectorListenerAdapter() {
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        System.out.println("成为Master角色");
                        Thread.sleep(3000);
                        System.out.println("完成Master操作，释放Master权利");
                    }
                });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}

