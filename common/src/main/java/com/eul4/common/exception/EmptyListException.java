package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;

public class EmptyListException extends CommonException
{
	public EmptyListException()
	{
		this(null);
	}
	
	protected EmptyListException(Throwable cause)
	{
		super(CommonMessage.EXCEPTION_EMPTY_LIST.withArgs(), cause);
	}
}
