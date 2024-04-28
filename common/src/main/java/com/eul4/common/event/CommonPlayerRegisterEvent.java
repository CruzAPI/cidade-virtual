package com.eul4.common.event;

import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class CommonPlayerRegisterEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final CommonPlayer oldCommonPlayer;
	private final CommonPlayer commonPlayer;
	
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