package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;

public class NumberFormatException extends CommonRuntimeException
{
	public NumberFormatException()
	{
		this(null);
	}
	
	public NumberFormatException(Throwable cause)
	{
		super(CommonMessage.EXCEPTION_NUMBER_FORMAT.withArgs(), cause);
	}
}
