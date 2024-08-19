package com.eul4.common.event;

import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
@Setter
public class TellEvent extends Event implements Cancellable
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final CommonPlayer commonPlayerSender;
	private final CommonPlayer commonPlayerTarget;
	
	private boolean cancelled;
	
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