package socket;

import java.util.concurrent.BlockingQueue;

public class LocalThread implements Runnable {
	private BlockingQueue<String> input;
	private BlockingQueue<String> output;
	private IDataReceiver job;
	public LocalThread(BlockingQueue<String> input,
			BlockingQueue<String> output, IDataReceiver job) {
				this.input = input;
				this.output = output;
				this.job = job;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String data = input.take();
				job.execute(data);
				output.put(job.getResponse());
			} catch (Exception e) {
				e.printStackTrace();
				try {
					output.put(e.getMessage());
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}

