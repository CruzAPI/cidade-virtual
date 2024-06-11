package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonAdmin;
import com.eul4.common.model.player.CommonPlayer;
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
		
		final CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!player.isOp())
		{
			commonPlayer.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		if(!(commonPlayer instanceof CommonAdmin commonAdmin))
		{
			commonPlayer.sendMessage(CommonMessage.COMMAND_BUILD_NEED_ADMIN);
			return false;
		}
		
		commonPlayer.sendMessage(commonAdmin.toggleBuild()
				? CommonMessage.COMMAND_BUILD_ENABLED
				: CommonMessage.COMMAND_BUILD_DISABLED);
		
		return true;
	}
}
