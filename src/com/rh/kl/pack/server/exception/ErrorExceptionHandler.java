package com.rh.kl.pack.server.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.rh.kl.pack.server.response.ErrorMessage;

@ControllerAdvice
public class ErrorExceptionHandler extends SimpleMappingExceptionResolver {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MessageException.class)
	public @ResponseBody ErrorMessage handleException(HttpServletRequest request, Exception ex) {
		return new ErrorMessage(((MessageException) ex).getErrorCode(), ((MessageException) ex).getErrorMessage());
	}
	
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AuthenticationException.class)
	public @ResponseBody ErrorMessage handleAuthenticationException(HttpServletRequest request, Exception ex) {
		return new ErrorMessage(((AuthenticationException) ex).getErrorCode(), ((AuthenticationException) ex).getErrorMessage());
	}
}
