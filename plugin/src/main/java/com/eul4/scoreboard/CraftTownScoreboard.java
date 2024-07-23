package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.TownPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import static com.eul4.i18n.TownScoreboardMessage.*;

public class CraftTownScoreboard extends CraftCommonScoreboard implements TownScoreboard
{
	private final TownPlayer townPlayer;
	
	private final Team likesTeam;
	private final Team dislikesTeam;
	private final Team hardnessTeam;
	private final Team footerTeam;
	
	public CraftTownScoreboard(TownPlayer townPlayer)
	{
		super(townPlayer, townPlayer.getPlugin().getServer().getScoreboardManager().getNewScoreboard());
		
		this.townPlayer = townPlayer;
		
		Objective objective = scoreboard.registerNewObjective("name", Criteria.DUMMY, Component.text("text"));
		
		objective.displayName(TITLE.translate(townPlayer));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		likesTeam = scoreboard.registerNewTeam("likesTeam");
		dislikesTeam = scoreboard.registerNewTeam("dislikesTeam");
		hardnessTeam = scoreboard.registerNewTeam("hardnessTeam");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
		
		String likesEntry = LIKES_ENTRY.translateToLegacyText(townPlayer);
		String dislikesEntry = DISLIKES_ENTRY.translateToLegacyText(townPlayer);
		String hardnessEntry = HARDNESS_ENTRY.translateToLegacyText(townPlayer);
		String footerEntry = FOOTER_ENTRY.translateToLegacyText(townPlayer);
		
		likesTeam.prefix(LIKES_PREFIX.translate(townPlayer));
		likesTeam.addEntry(likesEntry);
		likesTeam.suffix(LIKES_SUFFIX.translate(townPlayer, townPlayer.getTown()));
		
		dislikesTeam.prefix(DISLIKES_PREFIX.translate(townPlayer));
		dislikesTeam.addEntry(dislikesEntry);
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(townPlayer, townPlayer.getTown()));
		
		hardnessTeam.prefix(HARDNESS_PREFIX.translate(townPlayer));
		hardnessTeam.addEntry(hardnessEntry);
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(townPlayer, townPlayer.getTown()));
		
		footerTeam.prefix(FOOTER_PREFIX.translate(townPlayer));
		footerTeam.addEntry(footerEntry);
		footerTeam.suffix(FOOTER_SUFFIX.translate(townPlayer));
		
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
		likesTeam.suffix(LIKES_SUFFIX.translate(townPlayer, townPlayer.getTown()));
	}
	
	@Override
	public void updateDislikesTeam()
	{
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(townPlayer, townPlayer.getTown()));
	}
	
	@Override
	public void updateHardnessTeam()
	{
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(townPlayer, townPlayer.getTown()));
	}
}
