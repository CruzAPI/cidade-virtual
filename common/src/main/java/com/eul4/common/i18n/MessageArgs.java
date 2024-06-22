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
	
	public Component translate(Locale locale)
	{
		return message.translate(locale, args);
	}
	
	public Component translate(CommonPlayer commonPlayer)
	{
		return message.translate(commonPlayer, args);
	}
	
	public List<Component> translateLore(CommonPlayer commonPlayer)
	{
		return message.translateLore(commonPlayer, args);
	}
}
