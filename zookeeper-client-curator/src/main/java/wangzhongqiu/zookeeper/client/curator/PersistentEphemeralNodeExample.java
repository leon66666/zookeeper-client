package wangzhongqiu.zookeeper.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode.Mode;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.TimeUnit;

public class PersistentEphemeralNodeExample {
	private static final String PATH = "/example/ephemeralNode";
	private static final String PATH2 = "/example/node";

	public static void main(String[] args) {
		String zkConnStr = "127.0.0.1:2181";
		CuratorFramework client = null;
		PersistentEphemeralNode node = null;
		try {
			client = CuratorFrameworkFactory.newClient(zkConnStr,
					new ExponentialBackoffRetry(1000, 3));
			client.getConnectionStateListenable().addListener(
					new ConnectionStateListener() {

						@Override
						public void stateChanged(CuratorFramework client,
								ConnectionState newState) {
							System.out.println("client state:"
									+ newState.name());

						}
					});
			client.start();

			// http://zookeeper.apache.org/doc/r3.2.2/api/org/apache/zookeeper/CreateMode.html
			node = new PersistentEphemeralNode(client, Mode.EPHEMERAL, PATH,
					"test".getBytes());
			node.start();
			node.waitForInitialCreate(3, TimeUnit.SECONDS);
			String actualPath = node.getActualPath();
			System.out.println("node " + actualPath + " value: "
					+ new String(client.getData().forPath(actualPath)));

			client.create().forPath(PATH2, "persistent node".getBytes());
			System.out.println("node " + PATH2 + " value: "
					+ new String(client.getData().forPath(PATH2)));
			// KillSession.kill(client.getZookeeperClient().getZooKeeper(),
			// zkConnStr);
			// System.out.println("node " + actualPath + " doesn't exist: "
			// + (client.checkExists().forPath(actualPath) == null));
			// System.out.println("node " + PATH2 + " value: "
			// + new String(client.getData().forPath(PATH2)));

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			CloseableUtils.closeQuietly(node);
			CloseableUtils.closeQuietly(client);
		}

	}

}
