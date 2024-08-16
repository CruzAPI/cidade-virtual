package com.eul4.scoreboard;

import com.eul4.common.scoreboard.CraftCommonScoreboard;
import com.eul4.model.player.InitialScoreboardPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import static com.eul4.i18n.InitialScoreboardMessage.*;

public class CraftInitialScoreboard extends CraftCommonScoreboard implements InitialScoreboard
{
	private final InitialScoreboardPlayer initialScoreboardPlayer;
	
	private final Team helloTeam;
	private final Team welcome1Team;
	private final Team welcome2Team;
	private final Team useTown1Team;
	private final Team useTown2Team;
	private final Team footerTeam;
	
	public CraftInitialScoreboard(InitialScoreboardPlayer initialScoreboardPlayer)
	{
		super(initialScoreboardPlayer, initialScoreboardPlayer.getPlugin().getServer().getScoreboardManager().getNewScoreboard());
		
		this.initialScoreboardPlayer = initialScoreboardPlayer;
		
		helloTeam = scoreboard.registerNewTeam("helloTeam");
		welcome1Team = scoreboard.registerNewTeam("welcome1Team");
		welcome2Team = scoreboard.registerNewTeam("welcome2Team");
		useTown1Team = scoreboard.registerNewTeam("useTown1Team");
		useTown2Team = scoreboard.registerNewTeam("useTown2Team");
		footerTeam = scoreboard.registerNewTeam("footerTeam");
		
		helloTeam.suffix(HELLO_SUFFIX.translate(initialScoreboardPlayer, initialScoreboardPlayer.getPlayer().displayName()));
		welcome1Team.suffix(WELCOME_1_SUFFIX.translate(initialScoreboardPlayer));
		welcome2Team.suffix(WELCOME_2_SUFFIX.translate(initialScoreboardPlayer));
		useTown1Team.suffix(USE_TOWN_1_SUFFIX.translate(initialScoreboardPlayer));
		useTown2Team.suffix(USE_TOWN_2_SUFFIX.translate(initialScoreboardPlayer));
	}
	
	@Override
	public void registerScores()
	{
		Objective objective = scoreboard.registerNewObjective("name", Criteria.DUMMY, Component.text("text"));
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		String helloEntry = HELLO_ENTRY.translateLegacy(initialScoreboardPlayer);
		String welcome1Entry = WELCOME_1_ENTRY.translateLegacy(initialScoreboardPlayer);
		String welcome2Entry = WELCOME_2_ENTRY.translateLegacy(initialScoreboardPlayer);
		String useTown1Entry = USE_TOWN_1_ENTRY.translateLegacy(initialScoreboardPlayer);
		String useTown2Entry = USE_TOWN_2_ENTRY.translateLegacy(initialScoreboardPlayer);
		String footerEntry = FOOTER_ENTRY.translateLegacy(initialScoreboardPlayer);
		
		helloTeam.addEntry(helloEntry);
		welcome1Team.addEntry(welcome1Entry);
		welcome2Team.addEntry(welcome2Entry);
		useTown1Team.addEntry(useTown1Entry);
		useTown2Team.addEntry(useTown2Entry);
		footerTeam.addEntry(footerEntry);
		
		objective.displayName(TITLE.translate(initialScoreboardPlayer));
		
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
	
	@Override
	public InitialScoreboardPlayer getScoreboardPlayer()
	{
		return initialScoreboardPlayer;
	}
}
