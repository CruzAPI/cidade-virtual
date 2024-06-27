package com.eul4.common.event;

import com.eul4.common.model.inventory.Gui;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

@RequiredArgsConstructor
@Getter
public class GuiClickEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final InventoryClickEvent inventoryClickEvent;
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