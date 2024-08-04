package com.eul4.common.event;

import com.eul4.common.model.inventory.Gui;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

@RequiredArgsConstructor
@Getter
@Setter
public class GuiClickEvent extends Event implements Cancellable
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final InventoryClickEvent inventoryClickEvent;
	private final Gui gui;
	
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
	
	public void setAllCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
		inventoryClickEvent.setCancelled(cancelled);
	}
}