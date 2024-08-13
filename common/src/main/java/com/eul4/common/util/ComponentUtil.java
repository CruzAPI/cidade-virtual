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
		final String originalChatInput = chatInput;
		chatInput = chatInput.replace('&', '§');
		
		Component component = chatInput.contains("§")
				? Component.text(chatInput)
				: MiniMessage.miniMessage().deserialize(chatInput);
		
		return hasExtraSpaces(component) ? Component.text(originalChatInput) : component;
	}
	
	private static boolean hasExtraSpaces(Component component)
	{
		String plain = toPlain(component);
		return plain.startsWith(" ") || plain.endsWith(" ") || plain.contains("  ");
	}
}
