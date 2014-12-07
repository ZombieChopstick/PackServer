package com.rh.kl.pack.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class MessageException extends Exception {
	
	private static final long serialVersionUID = 138686376656533567L;
	@Getter @Setter private Integer errorCode;
	@Getter @Setter private String errorMessage;

}
