package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

@Getter
public class PermNotFoundInGroupException extends CommonException
{
	private final String permName;
	private final String groupName;
	
	public PermNotFoundInGroupException(String permName, String groupName)
	{
		this(permName, groupName, null);
	}
	
	protected PermNotFoundInGroupException(String permName, String groupName, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_PERM_NOT_FOUND_IN_GROUP.withArgs(permName, groupName), cause);
		this.permName = permName;
		this.groupName = groupName;
	}
}
