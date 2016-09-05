/*
 * Adam Wolf
 * CS283
 * Assignment 3 - Chat Program
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/*
 * This is a container for threads, where each instance of this is a new user.
 */
public class ClientThread extends Thread {
	
	private ClientImpl client = null;
	public DataInputStream streamIn = null;
	
	public ClientThread(ClientImpl client, Socket socket) {
		this.client = client;
		try {
			streamIn = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		start();
	}

	/*
	 * Reads user messages and sends them back to client for processing
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		Boolean moreMessages = Boolean.TRUE;
		while (moreMessages) {
			try {
				client.handleMessage(streamIn.readUTF());
			} catch (IOException e) {
				client.disconnect();
				moreMessages = Boolean.FALSE;
			}
		}
	}
}
