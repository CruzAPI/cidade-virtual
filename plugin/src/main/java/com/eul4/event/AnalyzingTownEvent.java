package com.eul4.event;

import com.eul4.model.player.RaidAnalyzer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class AnalyzingTownEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final RaidAnalyzer raidAnalyzer;
	
	public static HandlerList getHandlerList()
	{
		return HANDLER;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return HANDLER;
	}
}