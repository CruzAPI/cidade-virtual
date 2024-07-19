package com.eul4.authenticator.util;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerUtil
{
	public static boolean isCracked(ProxiedPlayer player)
	{
		return !isPremium(player);
	}
	
	public static boolean isPremium(ProxiedPlayer player)
	{
		return player.getUniqueId().version() == 4;
	}
}
