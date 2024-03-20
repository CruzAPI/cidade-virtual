package com.eul4.town.command;

import com.eul4.town.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class TestCommand implements TabExecutor
{
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender,
			Command command,
			String alias,
			String[] args)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender,
			Command command,
			String alias,
			String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
//		player.teleport(new Location(plugin.getCidadeVirtualWorld(), 0.0D, 60.0D, 0.0D));
		
		return false;
	}
}
