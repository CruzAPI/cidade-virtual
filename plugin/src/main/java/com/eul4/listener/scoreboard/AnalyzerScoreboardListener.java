package com.eul4.listener.scoreboard;

import com.eul4.Main;
import com.eul4.common.event.CommonPlayerRegisterEvent;
import com.eul4.common.event.CommonPlayerUnregisterEvent;
import com.eul4.event.*;
import com.eul4.model.player.PluginPlayer;
import com.eul4.model.player.spiritual.RaidAnalyzer;
import com.eul4.model.town.Town;
import com.eul4.model.town.structure.TownHall;
import com.eul4.scoreboard.AnalyzerScoreboard;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class AnalyzerScoreboardListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(LikeChangeEvent event)
	{
		update(event, AnalyzerScoreboard::updateLikesTeam);
	}
	
	@EventHandler
	public void on(DislikeChangeEvent event)
	{
		update(event, AnalyzerScoreboard::updateDislikesTeam);
	}
	
	@EventHandler
	public void on(CrownChangeEvent event)
	{
		update(event, AnalyzerScoreboard::updateCrownsTeam);
	}
	
	@EventHandler
	public void on(TownCapacityChangeEvent event)
	{
		update(event, AnalyzerScoreboard::updateLikesAndDislikesTeams);
	}
	
	@EventHandler
	public void on(TownHardnessChangeEvent event)
	{
		update(event, AnalyzerScoreboard::updateHardnessTeam);
	}
	
	@EventHandler
	public void on(AnalyzingTownEvent event)
	{
		event.getRaidAnalyzer().getScoreboard().updateAll();
	}
	
	@EventHandler
	public void on(StructureFinishEvent event)
	{
		if(!(event.getStructure() instanceof TownHall townHall))
		{
			return;
		}
		
		update(townHall.getTown(), AnalyzerScoreboard::updateTownHallLevelTeam);
	}
	
	@EventHandler
	public void on(CommonPlayerRegisterEvent event)
	{
		PluginPlayer pluginPlayer = (PluginPlayer) event.getCommonPlayer();
		
		update(pluginPlayer, AnalyzerScoreboard::updatePlayerStatusTeam);
	}
	
	@EventHandler
	public void on(CommonPlayerUnregisterEvent event)
	{
		PluginPlayer pluginPlayer = (PluginPlayer) event.getCommonPlayer();
		
		update(pluginPlayer, AnalyzerScoreboard::updatePlayerStatusTeam);
	}
	
	@EventHandler
	public void on(GenerateLikeEvent event)
	{
		update(event, AnalyzerScoreboard::updateLikesTeam);
	}
	
	@EventHandler
	public void on(GenerateDislikeEvent event)
	{
		update(event, AnalyzerScoreboard::updateDislikesTeam);
	}
	
	@EventHandler
	public void on(GeneratorsCapacityChangeEvent event)
	{
		update(event, AnalyzerScoreboard::updateLikesAndDislikesTeams);
	}
	
	private void update(PluginPlayer pluginPlayer, Consumer<? super AnalyzerScoreboard> action)
	{
		update(pluginPlayer.getTown(), action);
	}
	
	private void update(TownEvent townEvent, Consumer<? super AnalyzerScoreboard> action)
	{
		update(townEvent.getTown(), action);
	}
	
	private void update(@Nullable Town town, Consumer<? super AnalyzerScoreboard> action)
	{
		Optional.ofNullable(town)
				.map(Town::getAnalyzer)
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(action);
	}
}
