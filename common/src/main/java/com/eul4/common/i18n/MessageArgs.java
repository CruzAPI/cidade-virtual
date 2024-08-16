package com.eul4.common.i18n;

import com.eul4.common.model.player.CommonPlayer;
import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Getter
@Builder
public class MessageArgs
{
	private final TranslatableMessage translatableMessage;
	private final Object[] args;
	
	public MessageArgs(TranslatableMessage translatableMessage, Object... args)
	{
		this.translatableMessage = translatableMessage;
		this.args = args;
	}
	
	public MessageArgs moreArgs(Object... args)
	{
		Object[] combinedArgs = new Object[this.args.length + args.length];
		System.arraycopy(this.args, 0, combinedArgs, 0, this.args.length);
		System.arraycopy(args, 0, combinedArgs, this.args.length, args.length);
		return new MessageArgs(translatableMessage, combinedArgs);
	}
	
	public Component translate(ResourceBundle bundle)
	{
		return translatableMessage.translate(bundle, args);
	}
	
	public Component translate(CommonPlayer commonPlayer)
	{
		return translatableMessage.translate(commonPlayer, args);
	}
	
	public Component translate(Locale locale)
	{
		return translatableMessage.translate(locale, args);
	}
	
	public List<Component> translateLines(ResourceBundle bundle)
	{
		return translatableMessage.translateLines(bundle, args);
	}
	
	public List<Component> translateLines(CommonPlayer commonPlayer)
	{
		return translatableMessage.translateLines(commonPlayer, args);
	}
	
	public List<Component> translateLines(Locale locale)
	{
		return translatableMessage.translateLines(locale, args);
	}
	
	public String translatePlain(ResourceBundle bundle)
	{
		return translatableMessage.translatePlain(bundle, args);
	}
	
	public String translatePlain(CommonPlayer commonPlayer)
	{
		return translatableMessage.translatePlain(commonPlayer, args);
	}
	
	public String translatePlain(Locale locale)
	{
		return translatableMessage.translatePlain(locale, args);
	}
	
	public String translateLegacy(ResourceBundle bundle)
	{
		return translatableMessage.translateLegacy(bundle, args);
	}
	
	public String translateLegacy(CommonPlayer commonPlayer)
	{
		return translatableMessage.translateLegacy(commonPlayer, args);
	}
	
	public String translateLegacy(Locale locale)
	{
		return translatableMessage.translateLegacy(locale, args);
	}
}
