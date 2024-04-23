package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.player.TownPlayer;
import com.eul4.model.town.Town;
import com.eul4.type.player.PluginCommonPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class RaidCommand implements TabExecutor
{
	private final Main plugin;
	
	private final Random random = new Random();
	private final Map<Player, Set<Town>> skippedTowns = new HashMap<>();
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player)
				|| !(plugin.getPlayerManager().get(player) instanceof TownPlayer townPlayer))
		{
			return true;
		}
		
		if(!townPlayer.hasTown())
		{
			player.sendMessage("you do not have a town.");
			return true;
		}
		
		RaidAnalyzer raidAnalyzer = plugin.getPlayerManager().reregister(townPlayer, PluginCommonPlayerType.RAID_ANALYZER);
		
		player.sendMessage("Searching towns to raid...");
		
		findRandomTown(townPlayer).ifPresentOrElse(raidAnalyzer::analyzeTown,
				() -> player.sendMessage("No towns found to raid! :("));
		
		return false;
	}
	
	private void startRaid()
	{
	
	}
	
	public Optional<Town> findRandomTown(TownPlayer townPlayer)
	{
		Player player = townPlayer.getPlayer();
		Town attackerTown = townPlayer.getTown();
		
		Map<Integer, List<Town>> onlineTownsLeveled = new HashMap<>();
		Map<Integer, List<Town>> offlineTownsLeveled = new HashMap<>();
		
		for(Town town : plugin.getTownManager().getTowns().values())
		{
			if(town.isOwner(player) || hasSkipped(player, town))
			{
				continue;
			}
			
			List<Town> towns;
			
			if(town.isOnline())
			{
				towns = onlineTownsLeveled.computeIfAbsent(town.getLevel(), x -> new ArrayList<>());
			}
			else
			{
				towns = offlineTownsLeveled.computeIfAbsent(town.getLevel(), x -> new ArrayList<>());
			}
			
			towns.add(town);
		}
		
		return findNearestLeveledRandomTown(attackerTown.getLevel(), onlineTownsLeveled, offlineTownsLeveled);
	}
	
	private Optional<Town> findNearestLeveledRandomTown(final int level,
			final Map<Integer, List<Town>> onlineTownsLeveled,
			final Map<Integer, List<Town>> offlineTownsLeveled)
	{
		final int nearestOfflineLevel = findNearestLevel(level, offlineTownsLeveled.keySet());
		final int nearestOnlineLevel = findNearestLevel(level, onlineTownsLeveled.keySet());
		
		final List<Town> towns =
				(nearestOfflineLevel == nearestOnlineLevel ?
				random.nextBoolean() :
				nearestOnlineLevel > nearestOfflineLevel)
						? onlineTownsLeveled.getOrDefault(nearestOnlineLevel, Collections.emptyList())
						: offlineTownsLeveled.getOrDefault(nearestOfflineLevel, Collections.emptyList());
		
		return towns.isEmpty() ? Optional.empty() : Optional.of(towns.get(random.nextInt(towns.size())));
	}
	
	private int findNearestLevel(int townLevel, Set<Integer> availableLevels)
	{
		int nearest = -1;
		int lowestGap = Integer.MAX_VALUE;
		
		for(int availableLevel : availableLevels)
		{
			int gap = Math.abs(townLevel - availableLevel);
			
			if(gap < lowestGap)
			{
				nearest = availableLevel;
				lowestGap = gap;
			}
			else if(gap == lowestGap && availableLevel > nearest)
			{
				nearest = availableLevel;
			}
		}
		
		return nearest;
	}
	
	private boolean hasSkipped(Player player, Town town)
	{
		return skippedTowns.getOrDefault(player, Collections.emptySet()).contains(town);
	}
}
