package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import org.bukkit.event.HandlerList;

@Getter
public class LikeChangeEvent extends TownEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final int oldLikes;
	private final int newLikes;
	
	public LikeChangeEvent(Town town, int oldLikes, int newLikes)
	{
		super(town);
		this.oldLikes = oldLikes;
		this.newLikes = newLikes;
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