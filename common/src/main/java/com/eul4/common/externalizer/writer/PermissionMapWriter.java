package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.Permission;
import com.eul4.common.model.permission.PermissionMap;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.Map;

public class PermissionMapWriter extends ObjectWriter<PermissionMap>
{
	public PermissionMapWriter(Writers writers)
	{
		super(writers, PermissionMap.class);
	}
	
	@Override
	protected void writeObject(PermissionMap permissionMap) throws IOException
	{
		PermissionWriter permissionWriter = writers.getWriter(PermissionWriter.class);
		
		Map<String, Permission> permissions = permissionMap.getPermissions();
		
		out.writeInt(permissions.size());
		
		for(Permission permission : permissions.values())
		{
			permissionWriter.writeReference(permission);
		}
	}
}
