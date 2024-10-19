package com.eul4.command;

import com.eul4.Main;
import com.eul4.i18n.PluginRichMessage;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.eul4.i18n.PluginMessage.COMMAND_BALANCE_TRY_TOWN_COMMAND;

@RequiredArgsConstructor
public class BalanceCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "balance";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "bal", "saldo" };
	
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if(!(commandSender instanceof Player player))
		{
			return false;
		}
		
		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		Town town = pluginPlayer.getTown();
		
		if(town == null)
		{
			pluginPlayer.sendMessage(COMMAND_BALANCE_TRY_TOWN_COMMAND);
			return false;
		}
		
		pluginPlayer.sendMessage(PluginRichMessage.COMMAND_BALANCE_$TOWN, town);
		return true;
	}
}
