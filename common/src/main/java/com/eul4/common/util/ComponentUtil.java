package com.eul4.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ComponentUtil
{
	public static String toPlain(Component component)
	{
		return PlainTextComponentSerializer.plainText().serialize(component).replaceAll("§.?", "");
	}
	
	public static Component chatInputToComponent(String chatInput)
	{
		chatInput = chatInput.replace('&', '§');
		
		return chatInput.contains("§")
				? Component.text(chatInput)
				: MiniMessage.miniMessage().deserialize(chatInput);
	}
}
