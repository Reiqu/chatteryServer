package chatteryServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.Message;

public class UserThread extends Thread {

	private Server server;
	private Socket socket;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public UserThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;		
	}
	
	public void run() {
		startListener();
	}
	
	
	private void startListener() {
		System.out.println("New socket");
		System.out.println("Listen to new client..");
		
		createStreams();
	
		
		try {
			Message welcomeMessage = new Message("Server", "Willkommen im Chat", 1);
		
			send(welcomeMessage);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			listenToNewMessages();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createStreams() {
		
		// Erstelle Outputstream für diesen User
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Error in getting output stream: " + e.getMessage());
			e.printStackTrace();
		}
		
		// Erstelle Inputstream für diesen User
		try {
			in = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			System.err.println("Error in getting input stream: " + e.getMessage());
			e.printStackTrace();
		}			
	}
	
	public void listenToNewMessages() throws ClassNotFoundException, IOException {
		while (true) {
			System.out.println("Test");
			Message incomingMessage = (Message) in.readObject();
			
			System.out.println("Gotcha! -- " + incomingMessage.getName() + ": " + incomingMessage.getText());
			server.sendToAll(incomingMessage);
		}
	}
	
	
	public void send(Message message) throws IOException {
		System.out.println("Sending message: (" + message.getName() + ": " + message.getText() + ")");
		out.writeObject(message);
	}

	/**
	 * @return the Output
	 */
	public ObjectOutputStream getOutput() {
		return out;
	}

	/**
	 * @return the Input
	 */
	public ObjectInputStream getInput() {
		return in;
	}
}
