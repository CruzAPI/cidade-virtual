package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import net.kyori.adventure.text.Component;
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
		hardnessTeam = scoreboard.registerNewTeam("hardnessTeam");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
	}
	
	@Override
	public void registerScores()
	{
		registered = true;
		
		String playerStatusEntry = PLAYER_STATUS_ENTRY.translateLegacy(raidAnalyzer);
		String townHallLevelEntry = TOWN_HALL_LEVEL_ENTRY.translateLegacy(raidAnalyzer);
		String likesEntry = LIKES_ENTRY.translateLegacy(raidAnalyzer);
		String dislikesEntry = DISLIKES_ENTRY.translateLegacy(raidAnalyzer);
		String hardnessEntry = HARDNESS_ENTRY.translateLegacy(raidAnalyzer);
		String footerEntry = FOOTER_ENTRY.translateLegacy(raidAnalyzer);
		
		playerStatusTeam.addEntry(playerStatusEntry);
		townHallLevelTeam.addEntry(townHallLevelEntry);
		likesTeam.addEntry(likesEntry);
		dislikesTeam.addEntry(dislikesEntry);
		hardnessTeam.addEntry(hardnessEntry);
		footerTeam.addEntry(footerEntry);
		
		updateAll();
		
		objective.getScore("§0 ").setScore(10);
		objective.getScore(playerStatusEntry).setScore(9);
		objective.getScore("§1 ").setScore(8);
		objective.getScore(townHallLevelEntry).setScore(7);
		objective.getScore("§2 ").setScore(6);
		objective.getScore(likesEntry).setScore(5);
		objective.getScore(dislikesEntry).setScore(4);
		objective.getScore("§3 ").setScore(3);
		objective.getScore(hardnessEntry).setScore(2);
		objective.getScore("§4 ").setScore(1);
		objective.getScore(footerEntry).setScore(0);
	}
	
	@Override
	public void updateTitle()
	{
		objective.displayName(TITLE.translateOne(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updatePlayerStatusTeam()
	{
		playerStatusTeam.prefix(PLAYER_STATUS_PREFIX.translateOne(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getOwnerDisplayName)
				.orElse(Component.text("???"))));
		
		playerStatusTeam.suffix(PLAYER_STATUS_SUFFIX.translateOne(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getOwner)
				.map(OfflinePlayer::isOnline)
				.orElse(false)));
	}
	
	@Override
	public void updateTownHallLevelTeam()
	{
		townHallLevelTeam.suffix(TOWN_HALL_LEVEL_SUFFIX.translateOne(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getBuiltLevel)
				.orElse(0)));
	}
	
	@Override
	public void updateLikesTeam()
	{
		likesTeam.suffix(LIKES_SUFFIX.translateOne(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateDislikesTeam()
	{
		dislikesTeam.suffix(DISLIKES_SUFFIX.translateOne(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateHardnessTeam()
	{
		hardnessTeam.suffix(HARDNESS_SUFFIX.translateOne(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
}
