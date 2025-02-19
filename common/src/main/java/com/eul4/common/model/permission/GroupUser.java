package com.eul4.common.model.permission;

import com.eul4.common.Common;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class GroupUser implements Expirable
{
	private final UUID userUniqueId;
	private final TimedTick timedTick;
	
	@Builder
	public GroupUser(UUID userUniqueId, TimedTick timedTick)
	{
		this.userUniqueId = Objects.requireNonNull(userUniqueId);
		this.timedTick = Objects.requireNonNull(timedTick);
	}
	
	@Override
	public String getName(Common plugin)
	{
		return plugin.getServer().getOfflinePlayer(userUniqueId).getName();
	}
}
