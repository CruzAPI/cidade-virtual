package com.eul4.common.scoreboard;

import com.eul4.common.model.player.ScoreboardPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.scoreboard.Scoreboard;

import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class CraftCommonScoreboard implements CommonScoreboard
{
	protected final ScoreboardPlayer scoreboardPlayer;
	protected final Scoreboard scoreboard;
	
	private boolean registered;
	
	public CraftCommonScoreboard(ScoreboardPlayer scoreboardPlayer, Supplier<Scoreboard> scoreboardSupplier)
	{
		this(scoreboardPlayer, scoreboardSupplier.get());
	}
	
	@Override
	public ScoreboardPlayer getScoreboardPlayer()
	{
		return scoreboardPlayer;
	}
	
	@Override
	public Scoreboard getBukkitScoreboard()
	{
		return scoreboard;
	}
	
	@Override
	public final void registerIfNotRegistered()
	{
		if(registered)
		{
			return;
		}
		
		registered = true;
		registerScores();
	}
}
