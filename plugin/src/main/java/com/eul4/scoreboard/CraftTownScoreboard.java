package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.PluginScoreboardPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import static com.eul4.i18n.TownScoreboardMessage.*;

public class CraftTownScoreboard extends CraftCommonScoreboard implements TownScoreboard
{
	private final PluginScoreboardPlayer pluginScoreboardPlayer;
	
	private final Team likesTeam;
	private final Team dislikesTeam;
	private final Team hardnessTeam;
	private final Team footerTeam;
	
	private final Team helloTeam;
	private final Team welcome1Team;
	private final Team welcome2Team;
	private final Team useTown1Team;
	private final Team useTown2Team;
	
	public CraftTownScoreboard(PluginScoreboardPlayer pluginScoreboardPlayer)
	{
		super(pluginScoreboardPlayer, pluginScoreboardPlayer.getPlugin().getServer().getScoreboardManager().getNewScoreboard());
		
		this.pluginScoreboardPlayer = pluginScoreboardPlayer;
		
		Objective objective = scoreboard.registerNewObjective("name", Criteria.DUMMY, Component.text("text"));
		
		objective.displayName(TITLE.translate(pluginScoreboardPlayer));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		likesTeam = scoreboard.registerNewTeam("likesTeam");
		dislikesTeam = scoreboard.registerNewTeam("dislikesTeam");
		hardnessTeam = scoreboard.registerNewTeam("hardnessTeam");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
		
		helloTeam = scoreboard.registerNewTeam("helloTeam");
		welcome1Team = scoreboard.registerNewTeam("welcome1Team");
		welcome2Team = scoreboard.registerNewTeam("welcome2Team");
		useTown1Team = scoreboard.registerNewTeam("useTown1Team");
		useTown2Team = scoreboard.registerNewTeam("useTown2Team");
		
		String likesEntry = LIKES_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		String dislikesEntry = DISLIKES_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		String hardnessEntry = HARDNESS_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		String footerEntry = FOOTER_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		
		String helloEntry = HELLO_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		String welcome1Entry = WELCOME_1_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		String welcome2Entry = WELCOME_2_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		String useTown1Entry = USE_TOWN_1_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		String useTown2Entry = USE_TOWN_2_ENTRY.translateToLegacyText(pluginScoreboardPlayer);
		
		likesTeam.addEntry(likesEntry);
		dislikesTeam.addEntry(dislikesEntry);
		hardnessTeam.addEntry(hardnessEntry);
		footerTeam.addEntry(footerEntry);
		
		helloTeam.addEntry(helloEntry);
		helloTeam.suffix(HELLO_SUFFIX.translate(pluginScoreboardPlayer, pluginScoreboardPlayer.getPlayer().displayName()));
		
		welcome1Team.addEntry(welcome1Entry);
		welcome1Team.suffix(WELCOME_1_SUFFIX.translate(pluginScoreboardPlayer));
		
		welcome2Team.addEntry(welcome2Entry);
		welcome2Team.suffix(WELCOME_2_SUFFIX.translate(pluginScoreboardPlayer));
		
		useTown1Team.addEntry(useTown1Entry);
		useTown1Team.suffix(USE_TOWN_1_SUFFIX.translate(pluginScoreboardPlayer));
		
		useTown2Team.addEntry(useTown2Entry);
		useTown2Team.suffix(USE_TOWN_2_SUFFIX.translate(pluginScoreboardPlayer));
		
		updateAll();
		
		if(pluginScoreboardPlayer.hasTown())
		{
			objective.getScore("§0 ").setScore(6);
			objective.getScore(likesEntry).setScore(5);
			objective.getScore(dislikesEntry).setScore(4);
			objective.getScore("§1 ").setScore(3);
			objective.getScore(hardnessEntry).setScore(2);
			objective.getScore("§2 ").setScore(1);
			objective.getScore(footerEntry).setScore(0);
		}
		else
		{
			objective.getScore("§0 ").setScore(8);
			objective.getScore(helloEntry).setScore(7);
			objective.getScore(welcome1Entry).setScore(6);
			objective.getScore(welcome2Entry).setScore(5);
			objective.getScore("§1 ").setScore(4);
			objective.getScore(useTown1Entry).setScore(3);
			objective.getScore(useTown2Entry).setScore(2);
			objective.getScore("§2 ").setScore(1);
			objective.getScore(footerEntry).setScore(0);
		}
	}
	
	@Override
	public void updateLikesTeam()
	{
		likesTeam.suffix(LIKES_SUFFIX.translate(pluginScoreboardPlayer, pluginScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateDislikesTeam()
	{
		dislikesTeam.suffix(DISLIKES_SUFFIX.translate(pluginScoreboardPlayer, pluginScoreboardPlayer.getTown()));
	}
	
	@Override
	public void updateHardnessTeam()
	{
		hardnessTeam.suffix(HARDNESS_SUFFIX.translate(pluginScoreboardPlayer, pluginScoreboardPlayer.getTown()));
	}
	
	@Override
	public PluginScoreboardPlayer getScoreboardPlayer()
	{
		return pluginScoreboardPlayer;
	}
}
