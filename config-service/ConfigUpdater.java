import java.io.IOException;
import java.lang.InterruptedException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.KeeperException;

public class ConfigUpdater {
	
	public static final String PATH = "/config";
	public static final Integer MAX_SLEEP = 10000;
	public static final Integer MAX_VALUE = Integer.MAX_VALUE;
	
	private ActiveKeyValueStore store;
	private Random random = new Random();
	
	public ConfigUpdater(String hosts) 
	throws IOException, InterruptedException {
		store = new ActiveKeyValueStore();
		store.connect(hosts);
	}
	
	public void run()
	throws InterruptedException, KeeperException {
		while (true) {
			String value = random.nextInt(MAX_VALUE) + "";
			
			store.write(PATH, value);
			System.out.printf("Set %s to %s%n", PATH, value);
			
			TimeUnit.MILLISECONDS.sleep(random.nextInt(MAX_SLEEP));
		}
	}
	
	public static void main(String[] args)
	throws Exception {
		ConfigUpdater configUpdater = new ConfigUpdater(args[0]);
		configUpdater.run();
	}
}
