package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

@Getter
public class UserNotFoundInGroupException extends CommonException
{
	private final String userName;
	private final String groupName;
	
	public UserNotFoundInGroupException(String userName, String groupName)
	{
		this(userName, groupName, null);
	}
	
	protected UserNotFoundInGroupException(String userName, String groupName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_USER_NOT_FOUND_IN_GROUP.withArgs(userName, groupName), cause);
		this.userName = userName;
		this.groupName = groupName;
	}
}
