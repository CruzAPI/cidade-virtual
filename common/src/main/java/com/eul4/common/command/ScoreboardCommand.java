package com.eul4.common.command;

import com.eul4.common.Common;
import com.eul4.common.i18n.CommonMessage;
import com.eul4.common.model.player.CommonPlayer;
import com.eul4.common.model.player.ScoreboardPerformer;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ScoreboardCommand implements TabExecutor
{
	public static String COMMAND_NAME = "scoreboard";
	public static final String[] NAME_AND_ALIASES = new String[] { COMMAND_NAME, "score" };
	
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
		
		if(!(commonPlayer instanceof ScoreboardPerformer scoreboardPerformer))
		{
			commonPlayer.sendMessage(CommonMessage.CAN_NOT_PERFORM);
			return false;
		}
		
		return scoreboardPerformer.performScoreboard();
	}
}
