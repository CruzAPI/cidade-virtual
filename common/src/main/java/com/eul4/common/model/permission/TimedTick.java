package com.eul4.common.model.permission;

import com.eul4.common.Common;
import com.eul4.common.util.MathUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimedTick
{
	private long creationTick;
	private long durationTick;
	
	@Builder
	public TimedTick(long creationTick, long durationTick)
	{
		this.creationTick = creationTick;
		this.durationTick = durationTick;
	}
	
	public TimedTick(Common plugin, long durationTick)
	{
		this.creationTick = plugin.getTotalTick();
		this.durationTick = durationTick;
	}
	
	public boolean isValid(Common plugin)
	{
		return plugin.getServerTickCounter().getTotalTick() <= getExpirationTick();
	}
	
	public boolean isPermanent()
	{
		return durationTick == Long.MAX_VALUE;
	}
	
	public void incrementDuration(long ticks)
	{
		durationTick = MathUtil.addSaturated(durationTick, ticks);
	}
	
	public long getExpirationTick()
	{
		return MathUtil.addSaturated(creationTick, durationTick);
	}
	
	public long getRemainingTicks(Common plugin)
	{
		if(isPermanent())
		{
			return Long.MAX_VALUE;
		}
		
		return Math.max(-1L, getExpiratingTick() - plugin.getTotalTick());
	}
	
	public long getExpiratingTick()
	{
		return creationTick + durationTick;
	}
}
