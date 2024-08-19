package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

@Getter
public class GroupNotFoundInGroupException extends CommonException
{
	private final String subGroupName;
	private final String groupName;
	
	public GroupNotFoundInGroupException(String subGroupName, String groupName)
	{
		this(subGroupName, groupName, null);
	}
	
	protected GroupNotFoundInGroupException(String subGroupName, String groupName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_GROUP_NOT_FOUND_IN_GROUP.withArgs(subGroupName, groupName), cause);
		this.subGroupName = subGroupName;
		this.groupName = groupName;
	}
}
