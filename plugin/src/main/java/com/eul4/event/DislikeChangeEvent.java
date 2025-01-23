package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import org.bukkit.event.HandlerList;

@Getter
public class DislikeChangeEvent extends TownEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final int oldDislikes;
	private final int newDislikes;
	
	public DislikeChangeEvent(Town town, int oldDislikes, int newDislikes)
	{
		super(town);
		this.oldDislikes = oldDislikes;
		this.newDislikes = newDislikes;
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