package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.GroupGroup;
import com.eul4.common.type.player.Writers;

import java.io.IOException;

public class GroupGroupWriter extends ObjectWriter<GroupGroup>
{
	public GroupGroupWriter(Writers writers)
	{
		super(writers, GroupGroup.class);
	}
	
	@Override
	protected void writeObject(GroupGroup groupGroup) throws IOException
	{
		writers.getWriter(UUIDWriter.class).writeReference(groupGroup.getGroupUniqueId());
		writers.getWriter(TimedTickWriter.class).writeReference(groupGroup.getTimedTick());
	}
}
