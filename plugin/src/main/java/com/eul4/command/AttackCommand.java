package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.spiritual.RaidAnalyzer;
import com.eul4.model.player.physical.TownPlayer;
import com.eul4.type.player.SpiritualPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class AttackCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "attack";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "atacar" };
	
	private final Main plugin;
	
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
			player.sendMessage("you do not have a town."); //TODO message
			return true;
		}
		
		RaidAnalyzer raidAnalyzer = (RaidAnalyzer) plugin.getPlayerManager().register(townPlayer, SpiritualPlayerType.RAID_ANALYZER);
		
		raidAnalyzer.reroll();
		
		return false;
	}
}
