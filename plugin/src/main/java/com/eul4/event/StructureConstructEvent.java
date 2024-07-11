package com.eul4.event;

import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class StructureConstructEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Structure structure;
	
	public StructureConstructEvent(Structure structure)
	{
		super(true);
		this.structure = structure;
	}
	
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