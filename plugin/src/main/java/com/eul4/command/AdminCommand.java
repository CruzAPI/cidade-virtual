package com.eul4.command;

import com.eul4.Main;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.type.player.PlayerType;
import com.eul4.i18n.PluginMessage;
import com.eul4.model.player.Admin;
import com.eul4.type.player.PhysicalPlayerType;
import com.eul4.world.OverWorld;
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
			return false;
		}
		
		final CommonPlayer commonPlayer = plugin.getPlayerManager().get(player);
		
		if(!player.isOp())
		{
			commonPlayer.sendMessage(CommonMessage.YOU_DO_NOT_HAVE_PERMISSION);
			return false;
		}
		
		final CommonPlayer newCommonPlayer;
		
		if(commonPlayer instanceof Admin admin)
		{
			PlayerType playerType = commonPlayer.getCommonWorld() instanceof OverWorld overWorld
					&& overWorld.isSafeZone(player.getLocation())
					? PhysicalPlayerType.SPAWN_PLAYER
					: commonPlayer.getCommonWorld().getDefaultPlayerType();
			
			if(playerType == PhysicalPlayerType.TOWN_PLAYER && !admin.hasTown())
			{
				admin.sendMessage(PluginMessage.CAN_NOT_LEAVE_ADMIN_WITH_NO_TOWN);
				return false;
			}
			
			newCommonPlayer = plugin.getPlayerManager().register(commonPlayer, playerType);
			newCommonPlayer.sendMessage(CommonMessage.GAME_MODE_CHANGED, YELLOW, CommonMessage.PLAYER);
		}
		else
		{
			newCommonPlayer = plugin.getPlayerManager().register(commonPlayer, PhysicalPlayerType.ADMIN);
			newCommonPlayer.sendMessage(CommonMessage.GAME_MODE_CHANGED, RED, CommonMessage.ADMINISTRATOR);
		}
		
		return true;
	}
}
