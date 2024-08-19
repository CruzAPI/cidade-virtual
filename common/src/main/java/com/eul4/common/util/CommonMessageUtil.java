package com.eul4.common.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import org.bukkit.OfflinePlayer;

import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

@UtilityClass
public class CommonMessageUtil
{
	public static final String UNKNOWN = "???";
	
	public static Component playerName(Object arg)
	{
		return arg instanceof OfflinePlayer offlinePlayer
				? text(Optional.ofNullable(offlinePlayer.getName()).orElse(UNKNOWN))
				: argToComponent(arg);
	}
	
	public static Component displayName(Object arg)
	{
		return arg instanceof OfflinePlayer offlinePlayer
				? getOfflinePlayerDisplayName(offlinePlayer)
				: argToComponent(arg);
	}
	public static Component usageRequiredArg(Object arg)
	{
		return text("<")
				.append(argToComponent(arg))
				.append(text(">"));
	}

	public static Component usageRequiredVarArgs(Object arg)
	{
		return text("<")
				.append(argToComponent(arg))
				.append(text("...>"));
	}
	
	public static Component usageOptionalArg(Object arg)
	{
		return text("[")
				.append(argToComponent(arg))
				.append(text("]"));
	}
	
	public static Optional<Object> getArgument(Object[] args, int index)
	{
		try
		{
			return Optional.ofNullable(args[index]);
		}
		catch(Exception e)
		{
			return Optional.empty();
		}
	}
	
	public static Component argToComponent(Object arg)
	{
		return arg instanceof Component component ? component : text(arg.toString());
	}
	
	public static Component getOfflinePlayerDisplayName(OfflinePlayer offlinePlayer)
	{
		return getOfflinePlayerDisplayName(offlinePlayer, DARK_GRAY);
	}
	
	public static Component getOfflinePlayerDisplayName(OfflinePlayer offlinePlayer, StyleBuilderApplicable style)
	{
		return offlinePlayer.isOnline()
				? offlinePlayer.getPlayer().displayName()
				: text(Optional.ofNullable(offlinePlayer.getName()).orElse(UNKNOWN)).applyFallbackStyle(style);
	}
	
	public static String joinMessage(String[] args)
	{
		StringBuilder msg = new StringBuilder();
		
		for(String arg : args)
		{
			msg.append(arg).append(" ");
		}
		
		return msg.toString().trim();
	}
}
