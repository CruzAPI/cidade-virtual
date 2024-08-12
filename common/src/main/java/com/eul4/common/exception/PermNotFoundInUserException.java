package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

@Getter
public class PermNotFoundInUserException extends CommonException
{
	private final String permName;
	private final String userName;
	
	public PermNotFoundInUserException(String permName, String userName)
	{
		this(permName, userName, null);
	}
	
	protected PermNotFoundInUserException(String permName, String userName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_PERM_NOT_FOUND_IN_USER.withArgs(permName, userName), cause);
		this.permName = permName;
		this.userName = userName;
	}
}
