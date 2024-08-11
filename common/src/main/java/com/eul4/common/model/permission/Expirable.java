package com.eul4.common.model.permission;

import com.eul4.common.Common;
import org.bukkit.plugin.Plugin;

public interface Expirable
{
	TimedTick getTimedTick();
	
	default boolean isValid(Common plugin)
	{
		return getTimedTick().isValid(plugin);
	}
	
	default boolean isPermanent()
	{
		return getTimedTick().isPermanent();
	}
	
	default void incrementDuration(long ticks)
	{
		getTimedTick().incrementDuration(ticks);
	}
	
	default long getDurationTick()
	{
		return getTimedTick().getDurationTick();
	}
	
	default long getRemainingTick(Common plugin)
	{
		return getTimedTick().getRemainingTicks(plugin);
	}
	
	String getName(Plugin plugin);
}
