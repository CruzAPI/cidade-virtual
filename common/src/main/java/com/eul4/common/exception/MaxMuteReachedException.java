package com.eul4.common.exception;

import com.eul4.common.i18n.CommonMessage;

public class MaxMuteReachedException extends CommonException
{
	public MaxMuteReachedException(int limit)
	{
		this(limit, null);
	}
	
	protected MaxMuteReachedException(int limit, Throwable cause)
	{
		super(CommonMessage.EXCEPTION_MAX_MUTE_REACHED.withArgs(limit), cause);
	}
}
