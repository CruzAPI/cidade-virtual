package com.eul4.town.command;

import com.eul4.town.Main;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.town.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

import static com.eul4.town.i18n.PluginMessage.COMMAND_TOWN_USAGE;

@RequiredArgsConstructor
public class TownCommand implements TabExecutor
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
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Town town = plugin.getTownManager().getOrCreateNewTown(player.getUniqueId());
					player.teleport(town.getLocation().add(0.0D, 1.0D, 0.0D));
				}
			}.runTaskAsynchronously(plugin);
		}
		else
		{
			commonPlayer.sendMessage(COMMAND_TOWN_USAGE, alias);
		}
		
		return false;
	}
}
