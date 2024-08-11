package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.GroupUser;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.UUID;

public class GroupUserWriter extends ObjectWriter<GroupUser>
{
	public GroupUserWriter(Writers writers)
	{
		super(writers, GroupUser.class);
	}
	
	@Override
	protected void writeObject(GroupUser groupUser) throws IOException
	{
		UUID uuid = groupUser.getUserUniqueId();
		
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
		
		writers.getWriter(TimedTickWriter.class).writeReference(groupUser.getTimedTick());
	}
}
