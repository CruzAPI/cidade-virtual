package com.eul4.common.event;

import com.eul4.common.model.inventory.Gui;
import com.eul4.common.model.player.CommonPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class GuiOpenEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Gui gui;
	
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