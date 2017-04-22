package com.ronustine.splendid.simplify.exception;

public class PromptException extends ServiceException{

	private static final long serialVersionUID = 1L;
	public PromptException() {
		super();
	}

	public PromptException(String message, Throwable cause) {
		super(message, cause);
	}

	public PromptException(String message) {
		super(message);
	}

	public PromptException(Throwable cause) {
		super(cause);
	}


}
