package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

public class UserNotFoundException extends CommonException
{
	@Getter
	private final String userName;
	
	public UserNotFoundException(String userName)
	{
		this(userName, null);
	}
	
	protected UserNotFoundException(String userName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_USER_NOT_FOUND.withArgs(userName), cause);
		this.userName = userName;
	}
}
