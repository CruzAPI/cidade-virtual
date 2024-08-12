package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;

public class PageNotFoundException extends CommonException
{
	public PageNotFoundException()
	{
		this(null);
	}
	
	protected PageNotFoundException(Throwable cause)
	{
		super(CommonMessage.EXCEPTION_PAGE_NOT_FOUND.withArgs(), cause);
	}
}
