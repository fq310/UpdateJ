package socket;

import java.util.concurrent.BlockingQueue;

public class UpdateJob implements Runnable {
	private BlockingQueue<String> input;
	private BlockingQueue<String> output;
	private IDataReceiver job;
	public UpdateJob(BlockingQueue<String> input, BlockingQueue<String> output, IDataReceiver job) {
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
				output.add(job.getResponse());
			} catch (Exception e) {
				e.printStackTrace();
				output.add(e.getMessage());
				return;
			}
		}
	}
	
}