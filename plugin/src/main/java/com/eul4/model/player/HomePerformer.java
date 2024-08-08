package com.eul4.model.player;

import com.eul4.common.wrapper.Pitch;
import com.eul4.i18n.PluginMessage;
import com.eul4.wrapper.HomeMap;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public sealed interface HomePerformer extends HomeTabCompleter permits HomePerformer.InstantTeleport
{
	default List<String> onHomeTabComplete(String[] args)
	{
		return HomeTabCompleter.super.onTabComplete(args, true);
	}
	
	default boolean performHome(String[] args)
	{
		if(args.length == 0)
		{
			Set<String> homeNames = new LinkedHashSet<>();
			
			Location respawnPoint = getPlayer().getRespawnLocation();
			
			if(respawnPoint != null)
			{
				homeNames.add(SetHomePerformer.RESPAWN_HOME);
			}
			
			homeNames.addAll(getVanillaPlayerData().getHomeMap().keySet());
			
			if(homeNames.isEmpty())
			{
				sendMessage(PluginMessage.COMMAND_HOME_EMPTY_HOMES);
				return false;
			}
			
			sendMessage(PluginMessage.COMMAND_HOME_YOUR_HOMES, homeNames);
			return true;
		}
		else if(args.length == 1)
		{
			final String homeName = args[0];
			return execute(homeName);
		}
		else
		{
			sendMessage(PluginMessage.COMMAND_HOME_USAGE);
			return false;
		}
	}
	
	boolean execute(String homeName);
	
	default boolean checkPreconditions(String homeName)
	{
		if(SetHomePerformer.HOME_KEY_WORDS.contains(homeName))
		{
			if(getPlayer().getRespawnLocation() == null)
			{
				sendMessage(PluginMessage.COMMAND_HOME_RESPAWN_POINT_NOT_FOUND);
				return false;
			}
			else
			{
				return true;
			}
		}
		
		HomeMap homeMap = getVanillaPlayerData().getHomeMap();
		Location homeLocation = homeMap.get(homeName);
		
		if(homeLocation == null)
		{
			sendMessage(PluginMessage.COMMAND_HOME_HOME_NOT_FOUND, homeName);
			return false;
		}
		
		return true;
	}
	
	default boolean teleportToHome(String homeName)
	{
		if(!checkPreconditions(homeName))
		{
			return false;
		}
		
		final Location homeLocation;
		
		if(SetHomePerformer.HOME_KEY_WORDS.contains(homeName))
		{
			homeLocation = getPlayer().getRespawnLocation();
		}
		else
		{
			homeLocation = getVanillaPlayerData().getHomeMap().get(homeName);
		}
		
		getPlayer().teleport(homeLocation);
		sendMessage(PluginMessage.COMMAND_HOME_TELEPORTED_TO_HOME, homeName);
		getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, Pitch.min());
		return true;
	}
	
	non-sealed interface InstantTeleport extends HomePerformer
	{
		@Override
		default boolean execute(String homeName)
		{
			return teleportToHome(homeName);
		}
	}
	
	interface Channeling extends InstantTeleport, Channeler
	{
		@Override
		default boolean execute(String homeName)
		{
			if(!checkPreconditions(homeName))
			{
				return false;
			}
			
			channel(8L * 20L, () -> InstantTeleport.super.execute(homeName));
			return true;
		}
	}
}
