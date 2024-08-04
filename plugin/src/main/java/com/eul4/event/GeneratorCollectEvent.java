package com.eul4.event;

import com.eul4.model.town.Town;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public abstract class GeneratorCollectEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Town town;
	private final int amountCollected;
	
	@Getter(AccessLevel.NONE)
	@Setter
	private boolean sendMessage = true;
	
	public static HandlerList getHandlerList()
	{
		return HANDLER;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return HANDLER;
	}
	
	public boolean getSendMessage()
	{
		return sendMessage;
	}
}