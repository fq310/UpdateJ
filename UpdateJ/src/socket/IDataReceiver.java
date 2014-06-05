package socket;

public interface IDataReceiver {
	void execute(String data);
	String getResponse();
}
