package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.RaidAnalyzer;
import com.eul4.model.town.Town;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import static com.eul4.i18n.AnalyzerScoreboardMessage.*;

public class CraftAnalyzerScoreboard extends CraftCommonScoreboard implements AnalyzerScoreboard
{
	private final RaidAnalyzer raidAnalyzer;
	
	private final Objective objective;
	
	private final Team analyzingTeam;
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
		
		analyzingTeam = scoreboard.registerNewTeam("analyzingTeam");
		townHallLevelTeam = scoreboard.registerNewTeam("townHallLevelTeam");
		likesTeam = scoreboard.registerNewTeam("likesTeam");
		dislikesTeam = scoreboard.registerNewTeam("dislikesTeam");
		hardnessTeam = scoreboard.registerNewTeam("hardnessTeam");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
	}
	
	@Override
	public void register()
	{
		if(registered)
		{
			return;
		}
		
		registered = true;
		
		Town analyzingTown = raidAnalyzer.getAnalyzingTown();
		
		objective.displayName(TITLE.translate(raidAnalyzer, analyzingTown));
		
		String analyzingEntry = ANALYZING_ENTRY.translateToLegacyText(raidAnalyzer);
		String townHallLevelEntry = TOWN_HALL_LEVEL_ENTRY.translateToLegacyText(raidAnalyzer);
		String likesEntry = LIKES_ENTRY.translateToLegacyText(raidAnalyzer);
		String dislikesEntry = DISLIKES_ENTRY.translateToLegacyText(raidAnalyzer);
		String hardnessEntry = HARDNESS_ENTRY.translateToLegacyText(raidAnalyzer);
		String footerEntry = FOOTER_ENTRY.translateToLegacyText(raidAnalyzer);
		
		analyzingTeam.prefix(ANALYZING_PREFIX.translate(raidAnalyzer));
		analyzingTeam.addEntry(analyzingEntry);
		analyzingTeam.suffix(ANALYZING_SUFFIX.translate(raidAnalyzer, analyzingTown.getOwnerDisplayName()));
		
		townHallLevelTeam.prefix(TOWN_HALL_LEVEL_PREFIX.translate(raidAnalyzer));
		townHallLevelTeam.addEntry(townHallLevelEntry);
		townHallLevelTeam.suffix(TOWN_HALL_LEVEL_SUFFIX.translate(raidAnalyzer, analyzingTown.getBuiltLevel()));
		
		likesTeam.prefix(LIKES_PREFIX.translate(raidAnalyzer));
		likesTeam.addEntry(likesEntry);
		likesTeam.suffix(LIKES_SUFFIX.translate(raidAnalyzer, analyzingTown));
		
		dislikesTeam.prefix(DISLIKES_PREFIX.translate(raidAnalyzer));
		dislikesTeam.addEntry(dislikesEntry);
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(raidAnalyzer, analyzingTown));
		
		hardnessTeam.prefix(HARDNESS_PREFIX.translate(raidAnalyzer));
		hardnessTeam.addEntry(hardnessEntry);
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(raidAnalyzer, analyzingTown));
		
		footerTeam.prefix(FOOTER_PREFIX.translate(raidAnalyzer));
		footerTeam.addEntry(footerEntry);
		footerTeam.suffix(FOOTER_SUFFIX.translate(raidAnalyzer));
		
		objective.getScore("§0 ").setScore(10);
		objective.getScore(analyzingEntry).setScore(9);
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
		objective.displayName(TITLE.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateAnalyzingTeam()
	{
		analyzingTeam.suffix(ANALYZING_SUFFIX.translate(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getOwnerDisplayName)
				.orElse(Component.text("???"))));
	}
	
	@Override
	public void updateTownHallLevelTeam()
	{
		townHallLevelTeam.suffix(TOWN_HALL_LEVEL_SUFFIX.translate(raidAnalyzer, raidAnalyzer
				.findAnalyzingTown()
				.map(Town::getBuiltLevel)
				.orElse(0)));
	}
	
	@Override
	public void updateLikesTeam()
	{
		likesTeam.suffix(LIKES_SUFFIX.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateDislikesTeam()
	{
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
	
	@Override
	public void updateHardnessTeam()
	{
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(raidAnalyzer, raidAnalyzer.getAnalyzingTown()));
	}
}
