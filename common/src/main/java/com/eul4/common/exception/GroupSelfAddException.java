package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;
import lombok.Getter;

public class GroupSelfAddException extends CommonException
{
	public GroupSelfAddException()
	{
		this(null);
	}
	
	protected GroupSelfAddException(Throwable cause)
	{
		super(CommonMessage.EXCEPTION_GROUP_SELF_ADD_EXCEPTION.withArgs(), cause);
	}
}
