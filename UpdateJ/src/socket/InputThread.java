package socket;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class InputThread implements Runnable {
	private JSocket socket;
	private BlockingQueue<String> intput;
	public InputThread(BlockingQueue<String> intput, JSocket socket2) {
		this.intput = intput;
		socket = socket2;
	}
	@Override
	public void run() {
		while (true) {
			String command;
			try {
				command = socket.read();
				if (invalidCommand(command)) continue;
				intput.put(command);
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
				return;
			}
		}
	}
	private boolean invalidCommand(String command) {
		return command == null || command.length() == 0 || !command.matches("");
	}
	
}
