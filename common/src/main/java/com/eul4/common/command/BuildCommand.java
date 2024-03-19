package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.model.player.CommonAdmin;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BuildCommand implements TabExecutor
{
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender,
			Command command,
			String s,
			String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender,
			Command command,
			String s,
			String[] strings)
	{
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		if(!(plugin.getPlayerManager().get(player) instanceof CommonAdmin commonAdmin))
		{
			return true;
		}
		
		commonAdmin.toggleBuild();
		
		return true;
	}
}
