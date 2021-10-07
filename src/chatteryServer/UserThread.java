package chatteryServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.Message;
import shared.MessageType;

public class UserThread extends Thread {

	private Server server;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private boolean isActive = true;
	private int channel;
	private String username;

	public UserThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	// Behandle die komplette UserThread Logik im seperaten Prozess
	public void run() {
		startListener();
	}

	private void startListener() {
		// Erstelle Input und Output Streams

		createStreams();

		try {
			// Gucke immer, ob Nachrichten eingehen und behandle sie
			listenToNewMessages();
		} catch (java.net.SocketException e) {
			
			Message leaveMessage = new Message(
					"Info", 
					getUsername() + " hat den Channel verlassen",
					MessageType.Message, getChannel());
			server.sendToChannel(leaveMessage);
			setActive(false);
			interrupt();
			
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

	// Wenn Nachricht ankommt wird beim Type unterteilt, was es für eine Nachricht
	// ist und dementsprechend behandelt
	public void listenToNewMessages() throws ClassNotFoundException, IOException, java.net.SocketException {
		while (true) {
			// Lese input Message
			Message incomingMessage = (Message) in.readObject();

			// Teile Aufgaben auf für jeweilige Types der Nachrichten
			if (incomingMessage != null) {
				switch (incomingMessage.getType()) {
				case Message: {
					server.sendToChannel(incomingMessage);
					break;
				}
				case INITIAL: {
					handleINITIAL(incomingMessage);
					break;
				}
				case CHANGE_CHANNEL: {
					handleCHANGE_CHANNEL(incomingMessage);
					break;
				}
				case SET_NAME: {
					handleSET_NAME(incomingMessage);
					break;
				}
				}
			}

		}
	}

	private void handleINITIAL(Message message) {
		this.setChannel(message.getChannel());
		this.setUsername(message.getName());

		Message welcomeMessage = new Message("Info", this.getUsername() + " hat den Channel betreten",
				MessageType.Message, this.getChannel());

		server.sendToChannel(welcomeMessage);
	}

	private void handleCHANGE_CHANNEL(Message message) {
		System.out.println("Changing channel from " + this.getChannel() + " to " + message.getChannel());

		Message leaveMessage = new Message("Info", this.getUsername() + " hat den Channel verlassen",
				MessageType.Message, this.getChannel());

		this.setChannel(message.getChannel());

		Message joinMessage = new Message("Info", this.getUsername() + " hat den Channel betreten", MessageType.Message,
				this.getChannel());

		// Infomeldung an alle im alten und neuen Channel, dass die Person den Channel
		// wechselt
		server.sendToChannel(leaveMessage);
		server.sendToChannel(joinMessage);
	}

	private void handleSET_NAME(Message message) {
		System.out.println("Changing username from " + this.getUsername() + " to " + message.getName());

		// Infomeldung an alle im Channel, dass der Name geändert wurde
		Message changeNameMessage = new Message("Info",
				this.getUsername() + " hat seinen Namen zu " + message.getName() + " gewechselt", MessageType.Message,
				this.getChannel());
		this.setUsername(message.getName());
		server.sendToChannel(changeNameMessage);
	}

	public void send(Message message) throws IOException {
		System.out.println("Sending message: (" + message.getName() + ": " + message.getText() + ")");
		try {
			out.writeObject(message);			
		} catch (java.net.SocketException e) {
			interrupt();
			setActive(false);
		}
	
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

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
