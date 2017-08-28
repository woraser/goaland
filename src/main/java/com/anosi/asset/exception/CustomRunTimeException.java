package com.anosi.asset.exception;

/***
 * 自定义的异常，用来给用户反馈
 * @author jinyao
 *
 */
public class CustomRunTimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8309130528065851032L;

	public CustomRunTimeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomRunTimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CustomRunTimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CustomRunTimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CustomRunTimeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
	
}
