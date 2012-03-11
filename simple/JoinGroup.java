import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

public class JoinGroup extends ConnectionWatcher {
	
	public void join(String groupName, String memberName) 
	throws KeeperException, InterruptedException {
		String path = "/" + groupName + "/" + memberName;
		String createdPath = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
	
	public static void main(String[] args) 
	throws Exception {
		JoinGroup joinGroup = new JoinGroup();
		joinGroup.connect(args[0]);
		joinGroup.join(args[1], args[2]);
		
		System.out.println("Idling for " + Long.MAX_VALUE + " seconds...");
		Thread.sleep(Long.MAX_VALUE);
	}
	
}