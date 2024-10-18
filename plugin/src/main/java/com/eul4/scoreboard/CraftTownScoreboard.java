package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.TownScoreboardPlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import static com.eul4.i18n.TownScoreboardMessage.*;
import static net.kyori.adventure.text.Component.text;
import static org.bukkit.scoreboard.Criteria.DUMMY;

public class CraftTownScoreboard extends CraftCommonScoreboard implements TownScoreboard
{
	private final TownScoreboardPlayer townScoreboardPlayer;
	
	private final Team likesTeam;
	private final Team dislikesTeam;
	private final Team crownsTeam;
	private final Team hardnessTeam;
	private final Team footerTeam;
	
	private Objective objective;
	
	public CraftTownScoreboard(TownScoreboardPlayer townScoreboardPlayer)
	{
		super(townScoreboardPlayer, townScoreboardPlayer.getPlugin().getServer().getScoreboardManager().getNewScoreboard());
		
		this.townScoreboardPlayer = townScoreboardPlayer;
		
		likesTeam = scoreboard.registerNewTeam("likesTeam");
		dislikesTeam = scoreboard.registerNewTeam("dislikesTeam");
		crownsTeam = scoreboard.registerNewTeam("crownsTeam");
		hardnessTeam = scoreboard.registerNewTeam("hardnessTeam");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
	}
	
	public void registerScores()
	{
		final String entryDisambiguation = "§r§r";
		
		final String likesEntry = "§0" + entryDisambiguation;
		final String dislikesEntry = "§1" + entryDisambiguation;
		final String crownsEntry = "§2" + entryDisambiguation;
		final String hardnessEntry = "§3" + entryDisambiguation;
		final String footerEntry = "§4" + entryDisambiguation;
		
		likesTeam.addEntry(likesEntry);
		dislikesTeam.addEntry(dislikesEntry);
		crownsTeam.addEntry(crownsEntry);
		hardnessTeam.addEntry(hardnessEntry);
		footerTeam.addEntry(footerEntry);
		
		Objective objective = getOrRegisterObjective();
		
		updateAll();
		
		objective.getScore("§0 ").setScore(7);
		objective.getScore(likesEntry).setScore(6);
		objective.getScore(dislikesEntry).setScore(5);
		objective.getScore(crownsEntry).setScore(4);
		objective.getScore("§1 ").setScore(3);
		objective.getScore(hardnessEntry).setScore(2);
		objective.getScore("§2 ").setScore(1);
		objective.getScore(footerEntry).setScore(0);
	}
	
	private Objective getOrRegisterObjective()
	{
		return objective = objective == null
				? registerObjective()
				: objective;
	}
	
	private Objective registerObjective()
	{
		Objective objective = scoreboard.registerNewObjective("name", DUMMY, text("text"));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		return objective;
	}
	
	@Override
	public void updateTitle()
	{
		getOrRegisterObjective().displayName(TITLE.translate(townScoreboardPlayer));
	}
	
	@Override
	public void updateLikesTeam()
	{
		likesTeam.prefix(LIKES_PREFIX.translate(townScoreboardPlayer));
		likesTeam.suffix(LIKES_SUFFIX.translate(townScoreboardPlayer, townScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateDislikesTeam()
	{
		dislikesTeam.prefix(DISLIKES_PREFIX.translate(townScoreboardPlayer));
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(townScoreboardPlayer, townScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateCrownsTeam()
	{
		crownsTeam.prefix(CROWNS_PREFIX.translate(townScoreboardPlayer));
		crownsTeam.suffix(CROWNS_SUFFIX.translate(townScoreboardPlayer, townScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateHardnessTeam()
	{
		hardnessTeam.prefix(HARDNESS_PREFIX.translate(townScoreboardPlayer));
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(townScoreboardPlayer, townScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateFooterTeam()
	{
		footerTeam.prefix(FOOTER_PREFIX.translate(townScoreboardPlayer));
		footerTeam.suffix(FOOTER_SUFFIX.translate(townScoreboardPlayer));
	}
	
	@Override
	public TownScoreboardPlayer getScoreboardPlayer()
	{
		return townScoreboardPlayer;
	}
}
