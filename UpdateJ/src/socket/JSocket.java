package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JSocket {
	private ServerSocket server;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;

	public JSocket(int port) throws IOException {
		this.server = new ServerSocket(port);
		socket = server.accept();
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}
	
	public String read() throws IOException {
		return input.readUTF();
	}
	
	public void write(String data) throws IOException {
		output.writeUTF(data);
	}
	
	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			JSocket s = new JSocket(8080);
			System.out.println(s.read());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
