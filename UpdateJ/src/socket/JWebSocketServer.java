package socket;

import fi.iki.elonen.NanoWebSocketServer;
import fi.iki.elonen.WebSocket;

public class JWebSocketServer extends NanoWebSocketServer {
	private IDataReceiver receiver;

	@Override
	public WebSocket openWebSocket(IHTTPSession handshake) {
		return new JWebSocket(handshake, receiver);
	}

	public JWebSocketServer(int port, IDataReceiver receiver) {
		super(port);
		this.receiver = receiver;
	}

}
