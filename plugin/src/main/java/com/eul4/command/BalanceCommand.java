package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.TownPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BalanceCommand implements TabExecutor
{
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
			player.sendMessage("you do not have a town.");
			return true;
		}
		
		int likes = townPlayer.getTown().getLikes();
		int likeLimit = townPlayer.getTown().getLikeCapacity();
		int dislikes = townPlayer.getTown().getDislikes();
		int dislikeLimit = townPlayer.getTown().getDislikeCapacity();
		
		player.sendMessage(String.format("likes: %d/%d", likes, likeLimit));
		player.sendMessage(String.format("dislikes: %d/%d", dislikes, dislikeLimit));
		
		return false;
	}
}
