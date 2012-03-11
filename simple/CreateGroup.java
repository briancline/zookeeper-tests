import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

public class CreateGroup implements Watcher {
	private static final int SESSION_TIMEOUT = 5000;
	
	private ZooKeeper zk;
	private CountDownLatch connectedSignal = new CountDownLatch(1);
	
	public void connect(String hosts) 
	throws IOException, InterruptedException {
		System.out.println("Connecting...");
		zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
		connectedSignal.await();
	}
	
	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			System.out.println("Connected!");
			connectedSignal.countDown();
		}
	}
	
	public void create(String groupName) 
	throws KeeperException, InterruptedException {
		String path = "/" + groupName;
		
		try {
			System.out.println("Creating " + path);
			String createdPath = zk.create(path, null, 
				Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
			System.out.println("Created " + createdPath);
		}
		catch (NodeExistsException ex) {
			System.out.println("EXCEPTION: Node already exists! " + path);
		}
	}
	
	public void close() 
	throws InterruptedException {
		System.out.println("Closing connection");
		zk.close();
	}
	
	public static void main(String[] args) 
	throws Exception {
		CreateGroup createGroup = new CreateGroup();
		createGroup.connect(args[0]);
		createGroup.create(args[1]);
		createGroup.close();
	}
}
