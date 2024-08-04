package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class AttackStartEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Town town;
	
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