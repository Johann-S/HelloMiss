package com.jservoire.exceptions;

public class ParseException extends Exception 
{
	private static final long serialVersionUID = 1L;
	public ParseException(final String message){
		super(message);
	}
}
