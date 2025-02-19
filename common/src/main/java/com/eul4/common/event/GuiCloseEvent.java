package com.eul4.common.event;

import com.eul4.common.model.inventory.Gui;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

@RequiredArgsConstructor
@Getter
public class GuiCloseEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Gui gui;
	private final InventoryCloseEvent inventoryCloseEvent;
	
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