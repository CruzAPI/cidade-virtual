package com.eul4.common.model.permission;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

@Setter
@Getter
@Builder
public class Permission implements Expirable
{
	private String name;
	private TimedTick timedTick;
	
	@Override
	public String getName(Plugin plugin)
	{
		return name;
	}
}
