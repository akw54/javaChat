/*
 * Adam Wolf
 * CS283
 * Assignment 3 - Chat Program
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

	private ServerImpl server = null;
	private Socket socket;
	private int ID = -1;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	
	public ServerThread(ServerImpl server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.ID = socket.getPort();
	}
	
	/*
	 * Sends message to client
	 */
	public void send(String message) {
		try {
			streamOut.writeUTF(message);
			streamOut.flush();
		} catch (IOException e) {
			System.out.println("ERROR in ServerThread#send: " + e.getMessage());
			server.removeUser(ID);
		}
	}
	
	/*
	 * Reads from user when connected
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		Boolean isRunning = Boolean.TRUE;
		System.out.println(ID + " has connected.");
		while (isRunning) {
			try {
				String msg = streamIn.readUTF();
				String[] message = msg.split(" ");
				long[] encryptedMsg = new long[message.length];
				for(int i = 0; i < message.length; i++) {
					encryptedMsg[i] = Long.parseLong(message[i]);
				};
				String decryptedMsg = server.rsa.decrypt(encryptedMsg);
				server.handleUser(ID, decryptedMsg);
			} catch (IOException e) {
				server.removeUser(ID);
				isRunning = Boolean.FALSE;
			}
		}
	}
	
	/*
	 * Initialize input/output
	 */
	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	
	/*
	 * Closes socket/input/output
	 */
	public void close() throws IOException {
		if (socket != null) {
			socket.close();
		} else if (streamIn != null) {
			streamIn.close();
		} else if (streamOut != null) {
			streamOut.close();
		}
	}

	public int getID() {
		return ID;
	}
}
