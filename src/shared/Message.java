package shared;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private MessageType type;
	private String text;
	private int channel;
	
	public Message(String name, String text, MessageType type, int channel) {
		this.name = name;
		this.text = text;
		this.type = type;
		this.channel = channel;	
	}
	
	
	/*
	 *  Getter / Setter
	 */


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

}
