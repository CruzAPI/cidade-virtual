package com.eul4.common.externalizer.writer;

import com.eul4.common.model.permission.GroupGroup;
import com.eul4.common.model.permission.GroupGroupMap;
import com.eul4.common.type.player.Writers;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class GroupGroupMapWriter extends ObjectWriter<GroupGroupMap>
{
	public GroupGroupMapWriter(Writers writers)
	{
		super(writers, GroupGroupMap.class);
	}
	
	@Override
	protected void writeObject(GroupGroupMap groupGroupMap) throws IOException
	{
		GroupGroupWriter groupGroupWriter = writers.getWriter(GroupGroupWriter.class);
		HashMap<UUID, GroupGroup> groupGroups = groupGroupMap.getGroupGroups();
		
		out.writeInt(groupGroups.size());
		
		for(GroupGroup groupGroup : groupGroups.values())
		{
			groupGroupWriter.writeReference(groupGroup);
		}
	}
}
