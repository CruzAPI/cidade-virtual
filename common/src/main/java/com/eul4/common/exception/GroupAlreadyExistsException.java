package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

public class GroupAlreadyExistsException extends CommonException
{
	@Getter
	private final String groupName;
	
	public GroupAlreadyExistsException(String groupName)
	{
		this(groupName, null);
	}
	
	protected GroupAlreadyExistsException(String groupName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_GROUP_ALREADY_EXISTS.withArgs(groupName), cause);
		this.groupName = groupName;
	}
}
