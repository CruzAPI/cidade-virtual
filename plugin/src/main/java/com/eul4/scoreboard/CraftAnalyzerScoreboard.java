package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.spiritual.RaidAnalyzer;
import com.eul4.model.town.Town;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import static com.eul4.i18n.AnalyzerScoreboardMessage.*;

public class CraftAnalyzerScoreboard extends CraftCommonScoreboard implements AnalyzerScoreboard
{
	private final RaidAnalyzer raidAnalyzer;
	
	private final Objective objective;
	
	private final Team playerStatusTeam;
	private final Team townHallLevelTeam;
	private final Team likesTeam;
	private final Team dislikesTeam;
	private final Team crownsTeam;
	private final Team hardnessTeam;
	private final Team footerTeam;
	
	private boolean registered;
	
	public CraftAnalyzerScoreboard(RaidAnalyzer raidAnalyzer)
	{
		super(raidAnalyzer, raidAnalyzer.getPlugin().getServer().getScoreboardManager().getNewScoreboard());
		
		this.raidAnalyzer = raidAnalyzer;
		
		objective = scoreboard.registerNewObjective("a", Criteria.DUMMY, Component.empty());
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		playerStatusTeam = scoreboard.registerNewTeam("playerStatusTeam");
		townHallLevelTeam = scoreboard.registerNewTeam("townHallLevelTeam");
		likesTeam = scoreboard.registerNewTeam("likesTeam");
		dislikesTeam = scoreboard.registerNewTeam("dislikesTeam");
		crownsTeam = scoreboard.registerNewTeam("crownsTeam");
		hardnessTeam = scoreboard.registerNewTeam("hardnessTeam");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
	}
	
	@Override
	public void registerScores()
	{
		registered = true;
		
		String playerStatusEntry = "§0";
		String townHallLevelEntry = "§1";
		String likesEntry = "§2";
		String dislikesEntry = "§3";
		String crownsEntry = "§4";
		String hardnessEntry = "§5";
		String footerEntry = "§6";
		
		playerStatusTeam.addEntry(playerStatusEntry);
		townHallLevelTeam.addEntry(townHallLevelEntry);
		likesTeam.addEntry(likesEntry);
		dislikesTeam.addEntry(dislikesEntry);
		crownsTeam.addEntry(crownsEntry);
		hardnessTeam.addEntry(hardnessEntry);
		footerTeam.addEntry(footerEntry);
		
		updateAll();
		
		objective.getScore("§0 ").setScore(11);
		objective.getScore(playerStatusEntry).setScore(10);
		objective.getScore("§1 ").setScore(9);
		objective.getScore(townHallLevelEntry).setScore(8);
		objective.getScore("§2 ").setScore(7);
		objective.getScore(likesEntry).setScore(6);
		objective.getScore(dislikesEntry).setScore(5);
		objective.getScore(crownsEntry).setScore(4);
		objective.getScore("§3 ").setScore(3);
		objective.getScore(hardnessEntry).setScore(2);
		objective.getScore("§4 ").setScore(1);
		objective.getScore(footerEntry).setScore(0);
	}
	
	@Override
	public void updateTitle()
	{
		objective.displayName(TITLE.translate(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getOwner)
				.orElse(null)));
	}
	
	@Override
	public void updatePlayerStatusTeam()
	{
		playerStatusTeam.prefix(PLAYER_STATUS_PREFIX.translate(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getOwner)
				.orElse(null)));
		
		playerStatusTeam.suffix(PLAYER_STATUS_SUFFIX.translate(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getOwner)
				.map(OfflinePlayer::isOnline)
				.orElse(false)));
	}
	
	@Override
	public void updateTownHallLevelTeam()
	{
		townHallLevelTeam.prefix(TOWN_HALL_LEVEL_PREFIX.translate(raidAnalyzer));
		townHallLevelTeam.suffix(TOWN_HALL_LEVEL_SUFFIX.translate(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getBuiltLevel)
				.orElse(0)));
	}
	
	@Override
	public void updateLikesTeam()
	{
		likesTeam.prefix(LIKES_PREFIX.translate(raidAnalyzer));
		likesTeam.suffix(LIKES_SUFFIX.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateDislikesTeam()
	{
		dislikesTeam.prefix(DISLIKES_PREFIX.translate(raidAnalyzer));
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateCrownsTeam()
	{
		crownsTeam.prefix(CROWNS_PREFIX.translate(raidAnalyzer));
		crownsTeam.suffix(CROWNS_SUFFIX.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateHardnessTeam()
	{
		hardnessTeam.prefix(HARDNESS_PREFIX.translate(raidAnalyzer));
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateFooterTeam()
	{
		footerTeam.prefix(FOOTER_PREFIX.translate(raidAnalyzer));
		footerTeam.suffix(FOOTER_SUFFIX.translate(raidAnalyzer));
	}
}
