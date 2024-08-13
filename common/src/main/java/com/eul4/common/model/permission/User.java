package com.eul4.common.model.permission;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class User implements Permissible
{
	private final UUID uuid;
	private final PermissionMap permissionMap;
	
	private String name;
	
	@Builder
	public User(UUID uuid, PermissionMap permissionMap)
	{
		this.uuid = Objects.requireNonNull(uuid);
		this.permissionMap = Objects.requireNonNull(permissionMap);
	}
	
	public User(UUID uuid)
	{
		this.uuid = uuid;
		this.permissionMap = new PermissionMap();
	}
}
