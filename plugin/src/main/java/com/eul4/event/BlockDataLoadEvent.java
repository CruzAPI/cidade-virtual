package com.eul4.event;

import com.eul4.service.BlockData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class BlockDataLoadEvent extends Event
{
	private static final HandlerList HANDLER = new HandlerList();
	
	public enum Cause
	{
		ORE_VEIN, OTHER
	}
	
	private final Block block;
	private final BlockData blockData;
	private final Cause cause;
	
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