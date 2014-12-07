package com.rh.kl.pack.server.response;

import lombok.Getter;
import lombok.Setter;

public class ErrorMessage extends Message {

	private static final long serialVersionUID = 7515020544348730867L;
	@Getter @Setter private Integer errorCode;
	
	public ErrorMessage(Integer errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

}
