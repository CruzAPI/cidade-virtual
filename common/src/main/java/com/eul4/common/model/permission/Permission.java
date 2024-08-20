package com.eul4.common.model.permission;

import com.eul4.common.Common;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Permission implements Expirable
{
	private String name;
	private TimedTick timedTick;
	
	@Override
	public String getName(Common plugin)
	{
		return name;
	}
}
