package com.eul4.event;

import com.eul4.economy.Transaction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class TransactionExecuteEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Transaction<?> transaction;
	
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