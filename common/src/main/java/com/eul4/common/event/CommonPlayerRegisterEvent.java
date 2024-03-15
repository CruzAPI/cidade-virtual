package com.eul4.common.event;

import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class CommonPlayerRegisterEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final CommonPlayer commonPlayer;
	
	public static HandlerList getHandlerList()
	{
		return HANDLER;
	}
	
	@Override
	public @NotNull HandlerList getHandlers()
	{
		return HANDLER;
	}
}