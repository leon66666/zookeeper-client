package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;

/**
 * @author wangzhongqiu
 * @date 2018/5/7.
 * 测试工具类：是一个可以模拟Zookeeper集群的环境的工具类
 */
public class TestingClusterDemo {
    public static void main(String[] args) throws Exception {
        TestingCluster cluster = new TestingCluster(3);
        cluster.start();
        Thread.sleep(2000);

        TestingZooKeeperServer leader = null;
        for(TestingZooKeeperServer zs : cluster.getServers()){
            System.out.print(zs.getInstanceSpec().getServerId()+"-");
            System.out.print(zs.getQuorumPeer().getServerState()+"-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
            if( zs.getQuorumPeer().getServerState().equals( "leading" )){
                leader = zs;
            }
        }
        leader.kill();
        System.out.println( "--After leader kill:" );
        for(TestingZooKeeperServer zs : cluster.getServers()){
            System.out.print(zs.getInstanceSpec().getServerId()+"-");
            System.out.print(zs.getQuorumPeer().getServerState()+"-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
        }
        cluster.stop();
    }
}
