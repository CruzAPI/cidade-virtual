package com.eul4.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class ComponentUtil
{
	public static final Component CORRECT_SYMBOL = text("✔").color(DARK_GREEN);
	public static final Component INCORRECT_SYMBOL = text("❌").color(DARK_RED);
	public static final Component ALERT_SYMBOL = text("⚠").color(GOLD);
	
	public static String toPlain(Component component)
	{
		return PlainTextComponentSerializer.plainText().serialize(component).replaceAll("§.?", "");
	}
	
	public static Component chatInputToComponent(String chatInput)
	{
		chatInput = chatInput.replace('&', '§');
		
		return chatInput.contains("§")
				? text(chatInput)
				: MiniMessage.miniMessage().deserialize(chatInput);
	}
}
