package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.util.ThreadUtil;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
public class BaltopCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "baltop";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME };
	
	private final Set<UUID> waitingComputation = new HashSet<>();
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			if(!waitingComputation.add(player.getUniqueId()))
			{
				return false;
			}
			
			pluginPlayer.sendMessage(PluginMessage.COMMAND_BALTOP_WAIT_COMPUTATION);
			
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
			{
				ThreadUtil.sneakySleep(1500L);
				List<PlayerBalance> playerBalances = plugin.getTownManager()
						.getTowns()
						.values()
						.stream()
						.map(PlayerBalance::new)
						.sorted(PlayerBalance.getBaltopComparator())
						.toList();
				
				pluginPlayer.sendMessage(PluginMessage.COMMAND_BALTOP_$LIST, playerBalances);
				waitingComputation.remove(player.getUniqueId());
			});
			return true;
		}
		else
		{
			pluginPlayer.sendMessage(PluginMessage.GENERIC_COMMAND_NOARGS_USE_$ALIASES, aliases);
			return false;
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	public static class PlayerBalance
	{
		private final OfflinePlayer offlinePlayer;
		private final BigDecimal balance;
		
		public PlayerBalance(Town town)
		{
			this.offlinePlayer = town.getOwner();
			this.balance = town.getCalculatedCrownBalance();
		}
		
		private static Comparator<PlayerBalance> getBaltopComparator()
		{
			return (p1, p2) ->
			{
				final int reverseBalanceComparation = p2.balance.compareTo(p1.balance);
				final int nameComparation = p1.offlinePlayer.getName().compareTo(p2.offlinePlayer.getName());
				
				return reverseBalanceComparation != 0 ? reverseBalanceComparation : nameComparation;
			};
		}
	}
}
