package com.eul4.common.util;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@UtilityClass
public class LoggerUtil
{
	public static void info(Plugin plugin, String msg, Object... args)
	{
		info(true, plugin, msg, args);
	}
	
	public static void warning(Plugin plugin, String msg, Object... args)
	{
		log(true, plugin.getLogger(), Level.WARNING, msg, args);
	}
	
	public static void info(boolean flag, Plugin plugin, String msg, Object... args)
	{
		info(flag, plugin.getLogger(), msg, args);
	}
	
	public static void info(Logger logger, String msg, Object... args)
	{
		info(true, logger, msg, args);
	}
	
	public static void info(boolean flag, Logger logger, String msg, Object... args)
	{
		log(flag, logger, Level.INFO, msg, args);
	}
	
	public static void log(boolean flag, Logger logger, Level level, String msg, Object... args)
	{
		if(flag)
		{
			logger.log(level, MessageFormat.format(msg, args));
		}
	}
	
	public static void warning(Plugin plugin, Throwable cause, String msg, Object... args)
	{
		log(plugin, Level.WARNING, cause, msg, args);
	}
	
	public static void severe(Plugin plugin, Throwable cause, String msg, Object... args)
	{
		log(plugin, Level.SEVERE, cause, msg, args);
	}
	
	public static void log(Plugin plugin, Level level, Throwable cause, String msg, Object... args)
	{
		log(true, plugin.getLogger(), level, cause, msg, args);
	}
	
	public static void log(boolean flag, Logger logger, Level level, Throwable cause, String msg, Object... args)
	{
		if(flag)
		{
			logger.log(level, MessageFormat.format(msg, args), cause);
		}
	}
}
