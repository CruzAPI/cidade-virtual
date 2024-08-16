package com.eul4.common.model.console;

import com.eul4.common.i18n.Messageable;
import org.bukkit.command.ConsoleCommandSender;

public interface Console extends Messageable
{
	ConsoleCommandSender getConsoleCommandSender();
}
