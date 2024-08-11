package com.eul4.common.model.console;

import com.eul4.common.i18n.Message;
import com.eul4.common.i18n.Messageable;
import com.eul4.common.i18n.ResourceBundleHandler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Locale;

@RequiredArgsConstructor
public class CraftConsole implements Messageable
{
	private final ConsoleCommandSender consoleCommandSender;
	
	@Override
	public void sendMessage(Message message, Object... args)
	{
		String plainText = LegacyComponentSerializer.legacySection().serialize(message.translateOne(ResourceBundleHandler.DEFAULT_LOCALE, args));
		
		String[] lines = plainText.split("\\n");
		
		consoleCommandSender.sendMessage(lines);
	}
}
