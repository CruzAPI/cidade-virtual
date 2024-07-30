package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.TownScoreboardPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import static com.eul4.i18n.TownScoreboardMessage.*;

public class CraftTownScoreboard extends CraftCommonScoreboard implements TownScoreboard
{
	private final TownScoreboardPlayer townScoreboardPlayer;
	
	private final Team likesTeam;
	private final Team dislikesTeam;
	private final Team hardnessTeam;
	private final Team footerTeam;
	
	public CraftTownScoreboard(TownScoreboardPlayer townScoreboardPlayer)
	{
		super(townScoreboardPlayer, townScoreboardPlayer.getPlugin().getServer().getScoreboardManager().getNewScoreboard());
		
		this.townScoreboardPlayer = townScoreboardPlayer;
		
		Objective objective = scoreboard.registerNewObjective("name", Criteria.DUMMY, Component.text("text"));
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		likesTeam = scoreboard.registerNewTeam("likesTeam");
		dislikesTeam = scoreboard.registerNewTeam("dislikesTeam");
		hardnessTeam = scoreboard.registerNewTeam("hardnessTeam");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
		
		String likesEntry = LIKES_ENTRY.translateToLegacyText(townScoreboardPlayer);
		String dislikesEntry = DISLIKES_ENTRY.translateToLegacyText(townScoreboardPlayer);
		String hardnessEntry = HARDNESS_ENTRY.translateToLegacyText(townScoreboardPlayer);
		String footerEntry = FOOTER_ENTRY.translateToLegacyText(townScoreboardPlayer);
		
		likesTeam.addEntry(likesEntry);
		dislikesTeam.addEntry(dislikesEntry);
		hardnessTeam.addEntry(hardnessEntry);
		footerTeam.addEntry(footerEntry);
		
		updateAll();
		
		objective.displayName(TITLE.translate(townScoreboardPlayer));
		
		objective.getScore("§0 ").setScore(6);
		objective.getScore(likesEntry).setScore(5);
		objective.getScore(dislikesEntry).setScore(4);
		objective.getScore("§1 ").setScore(3);
		objective.getScore(hardnessEntry).setScore(2);
		objective.getScore("§2 ").setScore(1);
		objective.getScore(footerEntry).setScore(0);
	}
	
	@Override
	public void updateLikesTeam()
	{
		likesTeam.suffix(LIKES_SUFFIX.translate(townScoreboardPlayer, townScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateDislikesTeam()
	{
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(townScoreboardPlayer, townScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateHardnessTeam()
	{
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(townScoreboardPlayer, townScoreboardPlayer.getTown()));
	}
	
	@Override
	public TownScoreboardPlayer getScoreboardPlayer()
	{
		return townScoreboardPlayer;
	}
}
