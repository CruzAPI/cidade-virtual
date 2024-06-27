package com.eul4.common.i18n;

import java.util.List;

public interface Messageable
{
	void sendMessage(Message message, Object... args);
	
	default void sendMessage(boolean flag, Message message, Object... args)
	{
		if(flag)
		{
			sendMessage(message, args);
		}
	}
	
	default void sendMessage(MessageArgs message)
	{
		sendMessage(message.getMessage(), message.getArgs());
	}
	
	default void sendMessages(List<MessageArgs> messages)
	{
		messages.toArray(MessageArgs[]::new);
	}
	
	default void sendMessages(MessageArgs... messages)
	{
		for(MessageArgs message : messages)
		{
			sendMessage(message);
		}
	}
}
