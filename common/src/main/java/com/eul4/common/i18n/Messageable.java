package com.eul4.common.i18n;

public interface Messageable
{
	void sendMessage(TranslatableMessage translatableMessage, Object... args);
	
	default void sendMessage(boolean flag, TranslatableMessage translatableMessage, Object... args)
	{
		if(flag)
		{
			sendMessage(translatableMessage, args);
		}
	}
	
	default void sendMessage(MessageArgs message)
	{
		sendMessage(message.getTranslatableMessage(), message.getArgs());
	}
}
