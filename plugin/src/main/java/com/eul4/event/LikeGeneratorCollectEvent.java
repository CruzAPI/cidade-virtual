package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class LikeGeneratorCollectEvent extends GeneratorCollectEvent
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public LikeGeneratorCollectEvent(Town town, int amountCollected)
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