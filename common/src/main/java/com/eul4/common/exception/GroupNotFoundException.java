package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

public class GroupNotFoundException extends CommonException
{
	@Getter
	private final String groupName;
	
	public GroupNotFoundException(String groupName)
	{
		this(groupName, null);
	}
	
	protected GroupNotFoundException(String groupName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_GROUP_NOT_FOUND.withArgs(groupName), cause);
		this.groupName = groupName;
	}
}
