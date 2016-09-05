/*
 * Adam Wolf
 * CS283
 * Assignment 3 - Chat Program
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientImpl {
	private Socket cSocket = null;
	private Scanner  cReader   = null;
	private DataOutputStream cOutput = null;
	private ClientThread clientThread;
	public static final String EXIT = "/exit";	
	private Boolean isThread = Boolean.TRUE;
	private MiniRSAImpl rsa;

	public ClientImpl(String serverHost, int serverPort, long e, long c, long d, long dc) {
		rsa = new MiniRSAImpl(e, c, d);
		try {
			connect(serverHost, serverPort);
		} catch (Exception ex) {
			System.out.printf("ERROR in ClientImpl: %s\n", ex.getMessage());
		}
	}
	
	/*
	 * runThread: Sends console messages through output stream to server
	 * @see Server#runThread()
	 */
	public void runThread() {
		while (isThread) {
			try {
				StringBuilder b = new StringBuilder();
				for (long c: rsa.encrypt(cReader.nextLine())) {
					b.append(c);
					b.append(" ");
				}
				cOutput.writeUTF(b.toString());
				cOutput.flush();
			} catch (Exception e) {
				System.out.println("ERROR in ClientImpl#runThread: " + e.getMessage());
				disconnect();
			} 
		}
	}
	
	/*
	 * handle: Outputs messages received from server
	 * @see Client#handle(java.lang.String)
	 */
	public void handleMessage(String message) {
		message = (!message.equals(EXIT) ? message : cSocket.getLocalPort() + " is no longer chatting... Press ENTER to quit.");
		System.out.println(message);
	}
	
	/*
	 * Make the connection to the server and begin accepting new messages.
	 */
	public void connect(String serverHost, int serverPort) throws IOException {
		System.out.println("Attemping server connection...");
		cSocket = new Socket(serverHost, serverPort);
		System.out.printf("Connected to %s\n", cSocket.getRemoteSocketAddress());
		System.out.printf("Your ID is: %s\nType \"/exit\" to disconnect.\n", cSocket.getLocalPort());
		cReader = new Scanner(System.in);
		cOutput = new DataOutputStream(cSocket.getOutputStream());
		clientThread = new ClientThread(this, cSocket);
	}
		
	/*
	 * stop: Stops threads and closes input/output streams
	 * @see Client#stop()
	 */
	public void disconnect() {
		try {
			isThread = Boolean.FALSE;
			cReader.close();
			cOutput.close();
			cSocket.close();
		} catch (IOException e) {
			System.out.println("ERROR in ClientImpl#disconnect: " + e.getMessage());
			try {
				clientThread.streamIn.close();
			} catch (IOException ex) {
				System.out.println("ERROR: " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		ClientImpl client;
		String hostname;
		Integer port;
		long e, c, d, dc;
		
		if (args.length == 6) {
			hostname = args[0];
			port = Integer.parseInt(args[1]);
			e = Integer.parseInt(args[2]);
			c = Integer.parseInt(args[3]);
			d = Integer.parseInt(args[4]);
			dc = Integer.parseInt(args[5]);
			client = new ClientImpl(hostname, port, e, c, d, dc);
			client.runThread();
		} else {
			System.out.println("ERROR: Invalid number of arguments.");
		}
	}
}
