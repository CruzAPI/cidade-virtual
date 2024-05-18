package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.player.TownPlayer;
import com.eul4.type.player.SpiritualPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class RaidCommand implements TabExecutor
{
	private final Main plugin;
	
	private final Random random = new Random();
	
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
		
		RaidAnalyzer raidAnalyzer = (RaidAnalyzer) plugin.getPlayerManager().register(townPlayer, SpiritualPlayerType.RAID_ANALYZER);
		
		raidAnalyzer.reset();
		raidAnalyzer.reroll();
		
		return false;
	}
}
