package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonAdmin;
import com.eul4.common.model.player.CommonPlayer;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
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
	private final Common plugin;
	
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
		
		final CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!player.isOp())
		{
			commonPlayer.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return true;
		}
		
		final CommonPlayer newCommonPlayer;
		
		if(commonPlayer instanceof CommonAdmin)
		{
			newCommonPlayer = plugin.getPlayerManager().reregister(commonPlayer, plugin.getDefaultCommonPlayerType());
			newCommonPlayer.sendMessage(CommonMessage.GAME_MODE_CHANGED, YELLOW, CommonMessage.PLAYER);
		}
		else
		{
			newCommonPlayer = plugin.getPlayerManager().reregister(commonPlayer, plugin.getDefaultCommonAdminPlayerType());
			newCommonPlayer.sendMessage(CommonMessage.GAME_MODE_CHANGED, RED, CommonMessage.ADMINISTRATOR);
		}
		
		newCommonPlayer.reset();
		
		return true;
	}
}
