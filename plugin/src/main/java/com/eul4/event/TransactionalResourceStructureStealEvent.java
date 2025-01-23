package com.eul4.event;

import com.eul4.model.town.structure.Structure;
import com.eul4.model.town.structure.TransactionalResourceStructure;
import com.eul4.wrapper.TransactionalResource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class TransactionalResourceStructureStealEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final TransactionalResource<?> transactionalResource;
	
	public TransactionalResourceStructure getStructure()
	{
		return transactionalResource.getStructure();
	}
	
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