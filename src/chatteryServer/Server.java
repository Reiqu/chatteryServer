package chatteryServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import shared.Message;

public class Server {

	private final int port = 8080;
	private ArrayList<UserThread> userThreads = new ArrayList<UserThread>();
	private ServerSocket serverSocket;
	
	public static void main(String[] args) {
		Server server = new Server();
		
		server.run();
		
	}

	// Läuft im seperaten Prozess
	private void run() {
		System.out.println("Server is starting..");
		
		try {
			
			// Erstelle den Server Socket
			createSocket();
			System.out.println("Socket created.\nListening to clients..");

			
			// Gucke, ob es Verbindungen gibt
			while(true) {
				listen();				
			}
		} catch (IOException e) {
			System.err.println("Error in run(): " + e.getMessage());
			
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
					System.out.println("Socket closed");
				
				} catch(IOException e) {
					System.out.println("Error closing socket: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	
	// Erstelle Socket
	private void createSocket() {
		try {
			ServerSocket socket = new ServerSocket(port);

			this.serverSocket = socket;
			
		} catch(IOException e) {
			
			System.err.println("Error in creating socket: " + e.getMessage());
			e.printStackTrace();
			this.serverSocket = null;
			
		}
	}
	
	
	
	private void listen() throws IOException {
		
		// Nehme Socketverbindung an und erstelle einen eigenen Thread für diesen Nutzer
		Socket userSocket = this.serverSocket.accept();
		UserThread newUser = new UserThread(userSocket, this);
			
		userThreads.add(newUser);
		newUser.start();	
		
	}
	
	
	/**
	 * @param message
	 */
	public void sendToChannel(Message message) {
		for(UserThread userThread : userThreads) {
			if (userThread.isActive() == true) {
				try {
					if (userThread.getChannel() == message.getChannel()) {
						userThread.send(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}
	}
}
