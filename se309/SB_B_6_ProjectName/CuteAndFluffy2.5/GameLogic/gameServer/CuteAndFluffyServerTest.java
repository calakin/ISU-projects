package gameServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CuteAndFluffyServerTest {
	private Socket socket;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	
	public CuteAndFluffyServerTest()
	{
		try {
			socket = new Socket("localhost", 8081);

			//ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());

			//may or may not work, depending on whose turn it is
			objOut.writeObject("MOVE player2 0 0 1 0");
			objOut.flush();
			objOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		CuteAndFluffyServerTest servletSimulator = new CuteAndFluffyServerTest();
	}
}