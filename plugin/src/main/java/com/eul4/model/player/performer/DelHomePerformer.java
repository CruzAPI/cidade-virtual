package com.eul4.model.player.performer;

import com.eul4.i18n.PluginMessage;
import com.eul4.wrapper.HomeMap;

import java.util.List;

public interface DelHomePerformer extends HomeTabCompleter
{
	default List<String> onDelHomeTabComplete(String[] args)
	{
		return HomeTabCompleter.super.onTabComplete(args, false);
	}
	
	default boolean performDelHome(String[] args)
	{
		if(args.length == 1)
		{
			final String homeName = args[0];
			
			HomeMap homeMap = getVanillaPlayerData().getHomeMap();
			
			if(SetHomePerformer.HOME_KEY_WORDS.contains(homeName)
					&& getPlayer().getRespawnLocation() != null)
			{
				sendMessage(PluginMessage.COMMAND_DELHOME_CAN_NOT_DELETE_RESPAWN);
				return false;
			}
			
			if(!homeMap.containsKey(homeName))
			{
				sendMessage(PluginMessage.COMMAND_HOME_HOME_NOT_FOUND, homeName);
				return false;
			}
			
			
			homeMap.remove(homeName);
			sendMessage(PluginMessage.COMMAND_DELHOME_HOME_DELETED, homeName);
			return true;
		}
		else
		{
			sendMessage(PluginMessage.COMMAND_DELHOME_USAGE);
			return false;
		}
	}
}
