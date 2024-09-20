package com.eul4.util;

import com.eul4.Main;
import com.eul4.common.i18n.MessageArgs;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.enums.Rarity;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public class BroadcastUtil
{
	private static final Random RANDOM = new Random();
	
	public static void broadcast(Main plugin, String permission, Consumer<Player> bonusAction, MessageArgs messageArgs)
	{
		plugin.getConsole().sendMessage(messageArgs);
		
		for(CommonPlayer commonPlayer : plugin.getPlayerManager().getAll())
		{
			if(commonPlayer.hasPermission(permission))
			{
				commonPlayer.sendMessage(messageArgs);
				bonusAction.accept(commonPlayer.getPlayer());
			}
		}
	}
}