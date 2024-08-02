package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import org.bukkit.event.HandlerList;

@Getter
public class DislikeGeneratorCollectEvent extends GeneratorCollectEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public DislikeGeneratorCollectEvent(Town town, int amountCollected)
	{
		super(town, amountCollected);
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