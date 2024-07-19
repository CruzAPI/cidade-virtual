package com.eul4.command;

import com.eul4.Main;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ShowBalancesCommand implements TabExecutor
{
	private final Main plugin;

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
	{
		return Collections.emptyList();
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		if (!(commandSender instanceof Player player))
		{
			return false;
		}

		PluginPlayer pluginPlayer = (PluginPlayer) plugin.getPlayerManager().get(player);
		Town town = pluginPlayer.getTown();

		if (town == null)
		{
			player.sendMessage(ChatColor.RED + "Você não está em uma cidade.");
			return false;
		}

		createScoreboard(player, town.getLikes(), town.getDislikes());
		return true;
	}

	private void createScoreboard(Player player, int likes, int dislikes)
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();

		Objective objective = board.registerNewObjective("balance", "dummy", ChatColor.GOLD + "Player Balance");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		Score likesScore = objective.getScore(ChatColor.GREEN + "Likes: ");
		likesScore.setScore(likes);

		Score dislikesScore = objective.getScore(ChatColor.RED + "Dislikes: ");
		dislikesScore.setScore(dislikes);

		player.setScoreboard(board);
	}
}
