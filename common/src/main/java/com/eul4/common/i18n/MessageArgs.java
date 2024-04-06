package com.eul4.common.i18n;

import lombok.Getter;

@Getter
public class MessageArgs
{
	private final Message message;
	private final Object[] args;
	
	public MessageArgs(Message message, Object... args)
	{
		this.message = message;
		this.args = args;
	}
}
