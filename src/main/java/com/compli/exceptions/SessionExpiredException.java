package com.compli.exceptions;

public class SessionExpiredException extends RuntimeException{
	public SessionExpiredException() {
		super("Session expired");
	}
}
