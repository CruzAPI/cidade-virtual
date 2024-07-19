package com.eul4.authenticator.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import static net.md_5.bungee.api.ChatColor.RED;

@UtilityClass
public class MessageUtil
{
	private static final String UNEXPECTED_ERROR = """
			Ocorreu um erro inesperado!
			Reporte isto para algum ADM no discord!""";
	
	public static void sendUnexpectedErrorMessage(CommandSender sender)
	{
		TextComponent textComponent = new TextComponent(UNEXPECTED_ERROR);
		textComponent.setColor(RED);
		sender.sendMessage(textComponent);
	}
}
