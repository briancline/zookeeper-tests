import java.io.IOException;
import java.lang.InterruptedException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigWatcher implements Watcher {
	
	private ActiveKeyValueStore store;
	
	public ConfigWatcher(String hosts)
	throws IOException, InterruptedException {
		store = new ActiveKeyValueStore();
		store.connect(hosts);
	}
	
	public void displayConfig()
	throws InterruptedException, KeeperException {
		Stat stat = new Stat();
		String value = store.read(ConfigUpdater.PATH, this, stat);
		System.out.printf("Read %s:%d as %s%n", ConfigUpdater.PATH, stat.getVersion(), value);
	}
	
	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeDataChanged) {
			try {
				displayConfig();
			}
			catch (InterruptedException e) {
				System.err.println("Interrupted. Exiting.");
				Thread.currentThread().interrupt();
			}
			catch (KeeperException e) {
				System.err.printf("KeeperException: %s Exiting.%n", e);
			}
		}
	}
	
	public static void main(String[] args)
	throws Exception {
		ConfigWatcher configWatcher = new ConfigWatcher(args[0]);
		configWatcher.displayConfig();
		
		/** This should only ever be killed, so sleep for quite a while **/
		Thread.sleep(Long.MAX_VALUE);
	}
}
