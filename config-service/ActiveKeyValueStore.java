import java.nio.charset.Charset;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

public class ActiveKeyValueStore extends ConnectionWatcher {
	
	private static final Charset CHARSET = Charset.forName("UTF-8");
	
	public String read(String path, Watcher watcher)
	throws InterruptedException, KeeperException {
		byte[] data = zk.getData(path, watcher, null);
		return new String(data, CHARSET);
	}
	
	public String read(String path, Watcher watcher, Stat stat)
	throws InterruptedException, KeeperException {
		byte[] data = zk.getData(path, watcher, stat);
		return new String(data, CHARSET);
	}
	
	public void write(String path, String value) 
	throws InterruptedException, KeeperException {
		Stat stat = zk.exists(path, false);
		
		if (stat == null) {
			zk.create(path, value.getBytes(CHARSET), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} 
		else {
			zk.setData(path, value.getBytes(CHARSET), -1);
		}
	}
}
