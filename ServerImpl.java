/*
 * Adam Wolf
 * CS283
 * Assignment 3 - Chat Program
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerImpl extends Thread implements Runnable {

	private final int MAX_USERS = 25;
	private ServerThread userList[] = new ServerThread[MAX_USERS];
	private ServerSocket socket = null;
	private int totalUsers = 0;
	private Boolean isThread = Boolean.TRUE;
	public MiniRSAImpl rsa;
	
	public ServerImpl (Integer port, long e, long c, long d, long dc) {
		try {
			this.socket = new ServerSocket(port);
			rsa = new MiniRSAImpl(e, c, d);
		} catch (IOException ex) {
			System.out.println("ERROR at ServerImpl with port " + port + ": " + ex.getMessage());
		}
	}
	
	/*
	 * Accept new connections and create new threads for each new socket
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		System.out.println("READY... \nWaiting for users to connect...");
		while (isThread) {
			try {
				Socket newSocket = socket.accept();
				userList[getTotalUsers()] = new ServerThread(this, newSocket);
				try {
					userList[getTotalUsers()].open();
					userList[getTotalUsers()].start();
					setTotalUsers(getTotalUsers() + 1);
				} catch (IOException e) {
					System.out.println("ERROR in ServerImpl#addThread: " + e.getMessage());
				}
			} catch (IOException e) {
				System.out.println("ERROR at ServerImpl#run: " + e.getMessage());
				isThread = Boolean.FALSE;
			}
		}
	}
	
	/*
	 * Linear search for users based on id, used to for handling user messages
	 */
	private int getUser(int localId) {
		for (int i = 0; i < getTotalUsers(); i++) {
			if (userList[i].getID() == localId) {
				return i;
			}
		}
		return -1;
	}
	
	/*
	 * Handles new messages by users, removes users if EXIT message is sent. Closes connection and thread to the user.
	 */
	public synchronized void handleUser(int ID, String message) {
		if (message.equals(ClientImpl.EXIT)) {
			userList[getUser(ID)].send(ClientImpl.EXIT);
			removeUser(ID);
		} else {
			for (int i = 0; i < getTotalUsers(); i++) {
				if (userList[i].getID() != ID) {
					userList[i].send(ID + ": " + message);
				}
			}
		}
		System.out.println(ID + " sent a message.");
	} 
	
	/*
	 * Removes users by id when a user disconnects from EXIT message.
	 */
	public synchronized void removeUser(int ID) {
		int index = getUser(ID);
		if (index > -1) { 
			ServerThread st = userList[index];
			System.out.println("User " + ID + " has left.");
			if (index < getTotalUsers() - 1) {
				for (int i = index + 1; i < getTotalUsers(); i++) {
					userList[i-1] = userList[i];
				}
			}
			setTotalUsers(getTotalUsers() - 1);
			try {
				st.close();
			} catch (IOException e) {
				System.out.println("ERROR at ServerImpl#removeUser: " + e.getMessage());
			}
		}
	}
	
	public int getTotalUsers() {
		return this.totalUsers;
	}
	
	public void setTotalUsers(int users) {
		this.totalUsers = users;
	}
	
	public static void main(String[] args) {
		ServerImpl server = null;
		if (args.length == 5) {
			int port = Integer.parseInt(args[0]);
			int e = Integer.parseInt(args[1]);
			int c = Integer.parseInt(args[2]);
			int d = Integer.parseInt(args[3]);
			int dc = Integer.parseInt(args[4]);
			server = new ServerImpl(port, e, c, d, dc);
			server.run();
		} else {
			System.out.println("ERROR at ServerImpl#main: Invalid number of arguments.");
		}
	}
	
}
