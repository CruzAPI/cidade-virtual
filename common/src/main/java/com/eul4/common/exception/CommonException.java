package com.eul4.common.exception;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.i18n.ResourceBundleHandler;
import lombok.Getter;

public abstract class CommonException extends Exception implements MessageableException
{
	@Getter
	protected final MessageArgs messageArgs;
	
	protected CommonException(MessageArgs messageArgs)
	{
		this(messageArgs, null);
	}
	
	protected CommonException(MessageArgs messageArgs, Throwable cause)
	{
		super(messageArgs.translateLegacy(ResourceBundleHandler.DEFAULT_LOCALE));
		this.messageArgs = messageArgs;
	}
}
