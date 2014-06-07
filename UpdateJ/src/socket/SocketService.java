package socket;

import java.io.IOException;

public class SocketService {
	public void start(IDataReceiver receiver) {
		JWebSocketServer socketServer = new JWebSocketServer(8080, receiver);
		try {
			socketServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server started, hit Enter to stop.\n");
        try {
            System.in.read();
        } catch (IOException ignored) {
        }
        socketServer.stop();
        System.out.println("Server stopped.\n");
	}
}
