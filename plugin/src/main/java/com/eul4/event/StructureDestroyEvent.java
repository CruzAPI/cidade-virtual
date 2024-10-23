package com.eul4.event;

import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class StructureDestroyEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Structure structure;
	
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