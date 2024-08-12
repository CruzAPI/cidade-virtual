package com.eul4.common.exception;

import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.i18n.ResourceBundleHandler;
import lombok.Getter;

public abstract class CommonRuntimeException extends RuntimeException implements MessageableException
{
	@Getter
	protected final MessageArgs messageArgs;
	
	protected CommonRuntimeException(MessageArgs messageArgs)
	{
		this(messageArgs, null);
	}
	
	protected CommonRuntimeException(MessageArgs messageArgs, Throwable cause)
	{
		super(messageArgs.translateLegacy(ResourceBundleHandler.DEFAULT_LOCALE));
		this.messageArgs = messageArgs;
	}
}
