package com.eul4.event;

import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
@RequiredArgsConstructor
public class StructureInteractEvent extends Event implements Cancellable
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Structure structure;
	
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