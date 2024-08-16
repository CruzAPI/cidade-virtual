package com.eul4.common.model.console;

import com.eul4.common.i18n.ResourceBundleHandler;
import com.eul4.common.i18n.TranslatableMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.ConsoleCommandSender;

@RequiredArgsConstructor
@Getter
public class CraftConsole implements Console
{
	private final ConsoleCommandSender consoleCommandSender;
	
	@Override
	public void sendMessage(TranslatableMessage translatableMessage, Object... args)
	{
		translatableMessage
				.translateLines(ResourceBundleHandler.DEFAULT_LOCALE, args)
				.forEach(consoleCommandSender::sendMessage);
	}
}
