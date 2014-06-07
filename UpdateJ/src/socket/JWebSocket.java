package socket;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.WebSocket;
import fi.iki.elonen.WebSocketFrame;
import fi.iki.elonen.WebSocketFrame.CloseCode;

public class JWebSocket extends WebSocket {
	private IDataReceiver receiver;

	public JWebSocket(IHTTPSession handshakeRequest, IDataReceiver receiver) {
		super(handshakeRequest);
		this.receiver = receiver;
	}

	@Override
	protected void onClose(CloseCode arg0, String arg1, boolean arg2) {
	}

	protected void onException(IOException e) {
	}

	@Override
	protected void onMessage(WebSocketFrame msg) {
		byte[] data = msg.getBinaryPayload();
		String str = new String(data);
		System.out.println("Received data: " + str);
		receiver.execute(str);
		try {
			this.send(receiver.getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPong(WebSocketFrame arg0) {
	}
}
