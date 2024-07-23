package com.eul4.event;

import com.eul4.model.town.Town;
import com.eul4.model.town.structure.Structure;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

@Getter
@RequiredArgsConstructor
public class LikeChangeEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final Town town;
	private final int oldLikes;
	private final int newLikes;
	
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