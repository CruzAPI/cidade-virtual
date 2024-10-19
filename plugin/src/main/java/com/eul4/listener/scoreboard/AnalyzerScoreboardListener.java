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

@RequiredArgsConstructor
public class AnalyzerScoreboardListener implements Listener
{
	private final Main plugin;
	
	@EventHandler
	public void on(DislikeChangeEvent event)
	{
		event.getTown().findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateDislikesTeam);
	}
	
	@EventHandler
	public void on(LikeChangeEvent event)
	{
		event.getTown().findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateLikesTeam);
	}
	
	@EventHandler
	public void on(TownCapacityChangeEvent event)
	{
		event.getTown().findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateLikesAndDislikesTeams);
	}
	
	@EventHandler
	public void on(TownHardnessChangeEvent event)
	{
		event.getTown().findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateHardnessTeam);
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
		
		townHall.getTown()
				.findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateTownHallLevelTeam);
	}
	
	@EventHandler
	public void on(CommonPlayerRegisterEvent event)
	{
		PluginPlayer pluginPlayer = (PluginPlayer) event.getCommonPlayer();
		
		pluginPlayer.findTown()
				.flatMap(Town::findAnalyzer)
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updatePlayerStatusTeam);
	}
	
	@EventHandler
	public void on(CommonPlayerUnregisterEvent event)
	{
		PluginPlayer pluginPlayer = (PluginPlayer) event.getCommonPlayer();
		
		pluginPlayer.findTown()
				.flatMap(Town::findAnalyzer)
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updatePlayerStatusTeam);
	}
	
	@EventHandler
	public void on(GenerateLikeEvent event)
	{
		event.getTown()
				.findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateLikesTeam);
	}
	
	@EventHandler
	public void on(GenerateDislikeEvent event)
	{
		event.getTown()
				.findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateDislikesTeam);
	}
	
	@EventHandler
	public void on(GeneratorsCapacityChangeEvent event)
	{
		event.getTown()
				.findAnalyzer()
				.map(RaidAnalyzer::getScoreboard)
				.ifPresent(AnalyzerScoreboard::updateLikesAndDislikesTeams);
	}
}
