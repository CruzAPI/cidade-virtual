package com.eul4.model.player;

import java.util.*;

public interface HomeTabCompleter extends PluginPlayer
{
	default List<String> onTabComplete(String[] args, boolean includeRespawnPoint)
	{
		if(args.length == 1)
		{
			Set<String> homeNames = getHomeNames(includeRespawnPoint);
			List<String> suggestions = new ArrayList<>();
			
			for(String homeName : homeNames)
			{
				if(homeName.toLowerCase().startsWith(args[0].toLowerCase()))
				{
					suggestions.add(homeName);
				}
			}
			
			return suggestions;
		}
		else
		{
			return Collections.emptyList();
		}
	}
	
	private Set<String> getHomeNames(boolean includeRespawnPoint)
	{
		Set<String> homeNames = new LinkedHashSet<>();
		
		if(includeRespawnPoint && getPlayer().getRespawnLocation() != null)
		{
			homeNames.add(SetHomePerformer.RESPAWN_HOME);
		}
		
		homeNames.addAll(getVanillaPlayerData().getHomeMap().keySet());
		
		return homeNames;
	}
}
