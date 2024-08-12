package com.eul4.common.model.permission;

import com.eul4.common.Common;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class PermissionMap extends ExpirableMap<String, Permission>
{
	private final HashMap<String, Permission> permissions;
	
	public PermissionMap()
	{
		this(new HashMap<>());
	}
	
	private PermissionMap(HashMap<String, Permission> permissions)
	{
		super(permissions);
		this.permissions = permissions;
	}
	
	public void put(Permission permission)
	{
		permissions.put(permission.getName(), permission);
	}
	
	public void compute(Common plugin, Permission permission)
	{
		computeExpirable(plugin, permission.getName(), permission);
	}
}
