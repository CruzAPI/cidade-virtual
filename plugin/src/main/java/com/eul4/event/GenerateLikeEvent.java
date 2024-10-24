package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import org.bukkit.event.HandlerList;

@Getter
public class GenerateLikeEvent extends TownEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public GenerateLikeEvent(Town town)
	{
		super(town);
	}
	
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