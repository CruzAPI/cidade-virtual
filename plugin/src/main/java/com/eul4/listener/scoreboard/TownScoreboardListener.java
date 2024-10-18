package com.eul4.listener.scoreboard;

import com.eul4.Main;
import com.eul4.event.*;
import com.eul4.model.player.TownScoreboardPlayer;
import com.eul4.model.town.Town;
import com.eul4.scoreboard.TownScoreboard;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class TownScoreboardListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(DislikeChangeEvent event)
	{
		update(event, TownScoreboard::updateDislikesTeam);
	}
	
	@EventHandler
	public void on(LikeChangeEvent event)
	{
		update(event, TownScoreboard::updateLikesTeam);
	}
	
	@EventHandler
	public void on(CrownChangeEvent event)
	{
		update(event, TownScoreboard::updateCrownsTeam);
	}
	
	@EventHandler
	public void on(TownCapacityChangeEvent event)
	{
		event.getTown().findPluginPlayer()
				.filter(TownScoreboardPlayer.class::isInstance)
				.map(TownScoreboardPlayer.class::cast)
				.map(TownScoreboardPlayer::getTownScoreboard)
				.ifPresent(TownScoreboard::updateLikesAndDislikesTeams);
	}
	
	@EventHandler
	public void on(TownHardnessChangeEvent event)
	{
		event.getTown().findPluginPlayer()
				.filter(TownScoreboardPlayer.class::isInstance)
				.map(TownScoreboardPlayer.class::cast)
				.map(TownScoreboardPlayer::getTownScoreboard)
				.ifPresent(TownScoreboard::updateHardnessTeam);
	}
	
	private void update(TownEvent townEvent, Consumer<? super TownScoreboard> action)
	{
		update(townEvent.getTown(), action);
	}
	
	private void update(Town town, Consumer<? super TownScoreboard> action)
	{
		town.findPluginPlayer()
				.filter(TownScoreboardPlayer.class::isInstance)
				.map(TownScoreboardPlayer.class::cast)
				.map(TownScoreboardPlayer::getTownScoreboard)
				.ifPresent(action);
	}
}
