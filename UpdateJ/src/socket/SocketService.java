package socket;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketService {
	private ExecutorService threadPool = Executors.newFixedThreadPool(3);
	public void start(IDataReceiver job) {
		BlockingQueue<String> input = new LinkedBlockingQueue<>();
		BlockingQueue<String> output = new LinkedBlockingQueue<>();
		try {
			JSocket socket = new JSocket(9527);
			threadPool.execute(new InputThread(input, socket));
			threadPool.execute(new OutputThread(output, socket));
			threadPool.execute(new LocalThread(input, output, job));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
