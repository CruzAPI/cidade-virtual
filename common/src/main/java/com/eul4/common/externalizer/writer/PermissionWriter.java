package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.Permission;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public class PermissionWriter extends ObjectWriter<Permission>
{
	public PermissionWriter(Writers writers)
	{
		super(writers, Permission.class);
	}
	
	@Override
	protected void writeObject(Permission permission) throws IOException
	{
		out.writeUTF(permission.getName());
		writers.getWriter(TimedTickWriter.class).writeReference(permission.getTimedTick());
	}
}
