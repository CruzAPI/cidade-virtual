package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.User;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.UUID;

public class UserWriter extends ObjectWriter<User>
{
	public UserWriter(Writers writers)
	{
		super(writers, User.class);
	}
	
	@Override
	protected void writeObject(User user) throws IOException
	{
		UUID uuid = user.getUuid();
		
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
		
		writers.getWriter(PermissionMapWriter.class).writeReference(user.getPermissionMap());
	}
}
