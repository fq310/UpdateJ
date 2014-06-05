package socket;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class OutputThread implements Runnable {
	private JSocket socket;
	private BlockingQueue<String> output;
	public OutputThread(BlockingQueue<String> output, JSocket socket2) {
		this.output = output;
		socket = socket2;
	}
	@Override
	public void run() {
		while (true) {
			try {
				String data = output.take();
				socket.write(data);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}