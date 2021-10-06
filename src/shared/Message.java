package shared;

import java.io.Serializable;

public class Message implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String text;
	private int channel;
	
	public Message(String name, String text, int channel) {
		this.name = name;
		this.text = text;
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
}
