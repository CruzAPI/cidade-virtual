package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Locale;

@Getter
@Builder
public class MessageArgs
{
	private final Message message;
	private final Object[] args;
	
	public MessageArgs(Message message, Object... args)
	{
		this.message = message;
		this.args = args;
	}
	
	public MessageArgs moreArgs(Object... args)
	{
		Object[] combinedArgs = new Object[this.args.length + args.length];
		System.arraycopy(this.args, 0, combinedArgs, 0, this.args.length);
		System.arraycopy(args, 0, combinedArgs, this.args.length, args.length);
		return new MessageArgs(message, combinedArgs);
	}
	
	public Component translateOne(Locale locale)
	{
		return message.translateOne(locale, args);
	}
	
	public Component translateOne(CommonPlayer commonPlayer)
	{
		return message.translateOne(commonPlayer, args);
	}
	
	public List<Component> translateLore(CommonPlayer commonPlayer)
	{
		return message.translateLore(commonPlayer, args);
	}
	
	public String translatePlain(Locale locale)
	{
		return message.translatePlain(locale, args);
	}
	
	public String translateLegacy(Locale locale)
	{
		return message.translateLegacy(locale, args);
	}
}
