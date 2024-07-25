package com.eul4.listener.scoreboard;

import com.eul4.Main;
import com.eul4.event.DislikeChangeEvent;
import com.eul4.event.LikeChangeEvent;
import com.eul4.event.TownCapacityChangeEvent;
import com.eul4.event.TownHardnessChangeEvent;
import com.eul4.model.player.TownScoreboardPlayer;
import com.eul4.scoreboard.TownScoreboard;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class TownScoreboardListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(DislikeChangeEvent event)
	{
		event.getTown().findPluginPlayer()
				.filter(TownScoreboardPlayer.class::isInstance)
				.map(TownScoreboardPlayer.class::cast)
				.map(TownScoreboardPlayer::getScoreboard)
				.ifPresent(TownScoreboard::updateDislikesTeam);
	}
	
	@EventHandler
	public void on(LikeChangeEvent event)
	{
		event.getTown().findPluginPlayer()
				.filter(TownScoreboardPlayer.class::isInstance)
				.map(TownScoreboardPlayer.class::cast)
				.map(TownScoreboardPlayer::getScoreboard)
				.ifPresent(TownScoreboard::updateLikesTeam);
	}
	
	@EventHandler
	public void on(TownCapacityChangeEvent event)
	{
		event.getTown().findPluginPlayer()
				.filter(TownScoreboardPlayer.class::isInstance)
				.map(TownScoreboardPlayer.class::cast)
				.map(TownScoreboardPlayer::getScoreboard)
				.ifPresent(TownScoreboard::updateLikesAndDislikesTeams);
	}
	
	@EventHandler
	public void on(TownHardnessChangeEvent event)
	{
		event.getTown().findPluginPlayer()
				.filter(TownScoreboardPlayer.class::isInstance)
				.map(TownScoreboardPlayer.class::cast)
				.map(TownScoreboardPlayer::getScoreboard)
				.ifPresent(TownScoreboard::updateHardnessTeam);
	}
}
