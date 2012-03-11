import java.util.List;
import java.lang.InterruptedException;
import org.apache.zookeeper.KeeperException;

public class DeleteGroup extends ConnectionWatcher {
	
	public void delete(String groupName)
	throws KeeperException, InterruptedException {
		String path = "/" + groupName;
		
		try {
			List<String> children = zk.getChildren(path, false);
			for (String child : children) {
				delete(groupName + "/" + child);
			}
			
			System.out.printf("Deleting znode %s\n", path);
			zk.delete(path, -1);
		}
		catch (KeeperException.NoNodeException e) {
			System.out.printf("Group %s does not exist\n", groupName);
			System.exit(1);
		}
	}
	
	public static void main(String[] args)
	throws Exception {
		DeleteGroup deleteGroup = new DeleteGroup();
		deleteGroup.connect(args[0]);
		deleteGroup.delete(args[1]);
		deleteGroup.close();
	}
}