package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.exception.CommonException;
import com.eul4.common.exception.MaxMuteReachedException;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.wrapper.UUIDHashSet;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.eul4.common.i18n.CommonMessage.*;

@RequiredArgsConstructor
public class DisableTellCommand implements TabExecutor
{
	public static final String COMMAND_NAME = "disable-tell";
	
	private final Common plugin;
	
	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
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
		
		CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(args.length == 0)
		{
			if(!commonPlayer.isTellEnabled())
			{
				commonPlayer.sendMessage(COMMAND_DISABLE_TELL_ALREADY_DISABLED);
				return false;
			}
			
			commonPlayer.setTellEnabled(false);
			commonPlayer.sendMessage(COMMAND_DISABLE_TELL_DISABLED);
			return true;
		}
		else
		{
			commonPlayer.sendMessage(COMMAND_DISABLE_TELL_USE, aliases);
			return false;
		}
	}
}