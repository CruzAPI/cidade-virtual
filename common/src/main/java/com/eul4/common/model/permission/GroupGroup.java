package com.eul4.common.model.permission;

import com.eul4.common.Common;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class GroupGroup implements Expirable
{
	private final UUID groupUniqueId;
	private final TimedTick timedTick;
	
	@Builder
	public GroupGroup(UUID groupUniqueId, TimedTick timedTick)
	{
		this.groupUniqueId = Objects.requireNonNull(groupUniqueId);
		this.timedTick = Objects.requireNonNull(timedTick);
	}
	
	@Override
	public String getName(Common plugin)
	{
		return Optional.ofNullable(plugin.getPermissionService().getGroup(this))
				.map(Group::getName)
				.orElse(null);
	}
}
