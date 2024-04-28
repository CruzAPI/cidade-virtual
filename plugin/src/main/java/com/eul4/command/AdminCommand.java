package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonAdmin;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.model.player.PluginPlayer;
import com.eul4.type.player.PluginCommonPlayerType;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

@RequiredArgsConstructor
public class AdminCommand implements TabExecutor
{
	private final Main plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		if(!(commandSender instanceof Player player))
		{
			return true;
		}
		
		final PluginPlayer pluginPlayer = plugin.getPlayerManager().get(player);
		
		if(!player.isOp())
		{
			pluginPlayer.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return true;
		}
		
		final CommonPlayer newCommonPlayer;
		
		if(pluginPlayer instanceof CommonAdmin)
		{
			newCommonPlayer = plugin.getPlayerManager().register(pluginPlayer, PluginCommonPlayerType.TOWN_PLAYER);
			newCommonPlayer.sendMessage(CommonMessage.GAME_MODE_CHANGED, YELLOW, CommonMessage.PLAYER);
		}
		else
		{
			newCommonPlayer = plugin.getPlayerManager().register(pluginPlayer, PluginCommonPlayerType.ADMIN);
			newCommonPlayer.sendMessage(CommonMessage.GAME_MODE_CHANGED, RED, CommonMessage.ADMINISTRATOR);
		}
		
		newCommonPlayer.reset();
		
		return true;
	}
}
