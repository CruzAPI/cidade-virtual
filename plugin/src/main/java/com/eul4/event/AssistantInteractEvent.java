package com.eul4.event;

import com.eul4.model.player.PluginPlayer;
import com.eul4.model.town.Town;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
@RequiredArgsConstructor
public class AssistantInteractEvent extends Event implements Cancellable
{
	private static final HandlerList HANDLER = new HandlerList();
	
	private final PluginPlayer pluginPlayer;
	private final Town town;
	
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