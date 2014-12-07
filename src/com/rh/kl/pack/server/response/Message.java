package com.rh.kl.pack.server.response;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	@Getter private String message;
	@Getter private String dateReceived;
	
	public Message(String message) {
		this.message = message;
		this.dateReceived = new Date().toString();
	}
}
