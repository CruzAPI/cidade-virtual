package com.eul4.exception;

import java.io.Serial;

public class InvalidCommonPlayerException extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	public InvalidCommonPlayerException(String message)
	{
		super(message);
	}
}
